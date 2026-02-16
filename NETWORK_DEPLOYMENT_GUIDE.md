# Network Deployment Guide - Stock Trading Application

This guide shows you how to deploy the application so others can access it.

## 🌐 Deployment Options

### Option 1: Local Network Access (LAN)
**Use Case:** Access from devices on the same WiFi/network
**Setup Time:** 5 minutes
**Cost:** Free

### Option 2: Public Internet Access
**Use Case:** Access from anywhere in the world
**Setup Time:** 15-30 minutes
**Cost:** Free tier available

---

## 📡 OPTION 1: Local Network Deployment (Quick)

### Step 1: Configure Application for Network Access

Update `application.properties`:

```properties
# Change this line:
server.address=0.0.0.0

# Keep port:
server.port=8080
```

**Or run with command line:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.address=0.0.0.0"
```

### Step 2: Find Your IP Address

**On Windows:**
```cmd
ipconfig
```
Look for "IPv4 Address" (e.g., 192.168.1.100)

**On Mac/Linux:**
```bash
ifconfig
# or
ip addr show
```
Look for your local IP (e.g., 192.168.1.100)

**On Linux (simple):**
```bash
hostname -I
```

### Step 3: Configure Firewall

**Windows Firewall:**
1. Open Windows Defender Firewall
2. Click "Advanced settings"
3. Click "Inbound Rules" → "New Rule"
4. Select "Port" → Next
5. Enter port 8080 → Next
6. Allow the connection → Next
7. Apply to all profiles → Next
8. Name it "Stock Trading App" → Finish

**Linux (Ubuntu) Firewall:**
```bash
sudo ufw allow 8080/tcp
sudo ufw reload
```

**Mac Firewall:**
1. System Preferences → Security & Privacy
2. Firewall tab → Firewall Options
3. Click "+" to add Java
4. Allow incoming connections

### Step 4: Start Application
```bash
cd stock-trading-app
mvn spring-boot:run
```

### Step 5: Access from Other Devices

**From any device on the same network:**
```
http://YOUR_IP_ADDRESS:8080
```

**Example:**
```
http://192.168.1.100:8080
```

### Testing Local Network Access

1. **On your computer:** Open http://localhost:8080
2. **On another device (same WiFi):** Open http://YOUR_IP:8080
3. Both should show the login page

---

## 🌍 OPTION 2: Public Internet Deployment

### Method A: Using ngrok (Easiest - Temporary)

**Best for:** Testing, demos, temporary access
**Limitations:** URL changes on restart (free tier)

#### Step 1: Install ngrok
```bash
# Download from https://ngrok.com/download
# or install via package manager:

# Mac:
brew install ngrok

# Linux:
wget https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-linux-amd64.tgz
tar xvzf ngrok-v3-stable-linux-amd64.tgz
sudo mv ngrok /usr/local/bin/
```

#### Step 2: Sign up for ngrok (free)
1. Go to https://ngrok.com/
2. Sign up for free account
3. Get your authtoken

#### Step 3: Configure ngrok
```bash
ngrok config add-authtoken YOUR_AUTH_TOKEN
```

#### Step 4: Start Your Application
```bash
cd stock-trading-app
mvn spring-boot:run
```

#### Step 5: Start ngrok (in new terminal)
```bash
ngrok http 8080
```

#### Step 6: Share the URL
ngrok will display a URL like:
```
Forwarding: https://abc123.ngrok.io -> http://localhost:8080
```

Share `https://abc123.ngrok.io` with anyone - they can access it from anywhere!

**Important:** This URL changes each time you restart ngrok (unless you have a paid plan).

---

### Method B: Deploy to Cloud (Heroku) - FREE

**Best for:** Permanent deployment, 24/7 access
**Limitations:** Sleeps after 30 min inactivity (free tier)

#### Step 1: Create Heroku Account
1. Go to https://www.heroku.com/
2. Sign up for free account
3. Install Heroku CLI: https://devcenter.heroku.com/articles/heroku-cli

#### Step 2: Prepare Application

Create `system.properties` in project root:
```properties
java.runtime.version=17
```

Create `Procfile` in project root:
```
web: java -jar target/stock-trading-app-1.0.0.jar
```

Update `application.properties`:
```properties
# Add these lines:
server.port=${PORT:8080}
spring.jpa.hibernate.ddl-auto=update
```

#### Step 3: Initialize Git
```bash
cd stock-trading-app
git init
git add .
git commit -m "Initial commit"
```

#### Step 4: Deploy to Heroku
```bash
# Login to Heroku
heroku login

# Create app
heroku create stock-trading-app-yourname

# Deploy
git push heroku master

# Open app
heroku open
```

Your app will be live at: `https://stock-trading-app-yourname.herokuapp.com`

---

### Method C: Deploy to Railway (Recommended - FREE)

**Best for:** Easy deployment, better free tier than Heroku
**Limitations:** 500 hours/month free (plenty for testing)

#### Step 1: Sign up
1. Go to https://railway.app/
2. Sign up with GitHub (free)

#### Step 2: Deploy
1. Click "New Project"
2. Select "Deploy from GitHub repo"
3. Connect your GitHub account
4. Push your code to GitHub first:
```bash
cd stock-trading-app
git init
git add .
git commit -m "Initial commit"
gh repo create stock-trading-app --public --source=. --remote=origin
git push -u origin main
```
5. Select your repository in Railway
6. Railway will auto-detect Spring Boot and deploy

#### Step 3: Add Environment Variables
In Railway dashboard:
- Click your project
- Go to "Variables" tab
- Add: `PORT=8080`

#### Step 4: Access Your App
Railway provides a URL like: `https://stock-trading-app.railway.app`

---

### Method D: Deploy to AWS Free Tier (Advanced)

**Best for:** Production-ready deployment, full control
**Limitations:** More complex setup

#### Quick AWS Deployment

1. **Create AWS Account** (12 months free tier)
2. **Launch EC2 Instance:**
   - Ubuntu Server 22.04 LTS
   - t2.micro (free tier)
   
3. **Connect to instance:**
```bash
ssh -i your-key.pem ubuntu@YOUR_AWS_IP
```

4. **Install Java & Maven:**
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven
```

5. **Upload & Run App:**
```bash
# Upload your app (from local machine):
scp -i your-key.pem -r stock-trading-app ubuntu@YOUR_AWS_IP:~/

# On AWS instance:
cd stock-trading-app
mvn clean package
java -jar target/stock-trading-app-1.0.0.jar
```

6. **Configure Security Group:**
   - In AWS Console
   - Edit security group
   - Add rule: Port 8080, Source: 0.0.0.0/0

7. **Access:** `http://YOUR_AWS_IP:8080`

---

## 🔒 Security Considerations

### For Production Deployment:

1. **Change Default Passwords**
```properties
# Update in application.properties or database
```

2. **Use HTTPS**
```properties
# Add SSL certificate
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-password
```

3. **Enable CORS for specific domains**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://yourdomain.com")
                .allowedMethods("GET", "POST");
    }
}
```

4. **Use Production Database**
   - Replace H2 with MySQL/PostgreSQL
   - Use environment variables for credentials

5. **Set Strong Admin Passwords**
   - Change default admin1/admin123
   - Use BCrypt encoded passwords

---

## 📱 Access URLs Summary

### Local Network:
```
http://YOUR_LOCAL_IP:8080
Example: http://192.168.1.100:8080
```

### ngrok (Temporary):
```
https://random-string.ngrok.io
Changes each restart (free tier)
```

### Heroku:
```
https://your-app-name.herokuapp.com
Permanent URL
```

### Railway:
```
https://your-app-name.railway.app
Permanent URL
```

### AWS:
```
http://YOUR_AWS_IP:8080
or with domain: https://yourdomain.com
```

---

## 🧪 Testing Multi-User Access

### Test Plan:
1. **Admin:** Login as admin1 from Device A
2. **User 1:** Login as user1 from Device B
3. **User 2:** Login as user2 from Device C

All three should be able to:
- Access simultaneously
- Perform independent actions
- See real-time updates in their accounts

---

## 🐛 Troubleshooting

### Issue: Can't access from other devices

**Solution:**
1. Check firewall settings
2. Verify application is binding to 0.0.0.0
3. Confirm devices are on same network
4. Try with IP instead of hostname

### Issue: Application crashes on startup

**Solution:**
1. Check Java version: `java -version` (need 17+)
2. Check port availability: `netstat -an | grep 8080`
3. Review application logs

### Issue: "Connection Refused"

**Solution:**
1. Ensure application is running
2. Check firewall rules
3. Verify port 8080 is open
4. Try: `telnet YOUR_IP 8080`

### Issue: Slow performance with multiple users

**Solution:**
1. Increase JVM memory:
```bash
java -Xmx512m -jar target/stock-trading-app-1.0.0.jar
```

2. Use production database (MySQL/PostgreSQL)

3. Enable connection pooling in application.properties:
```properties
spring.datasource.hikari.maximum-pool-size=10
```

---

## 📊 Recommended Deployment for Your Use Case

### For Dissertation/Testing (2 Admins + 30 Users):

**Recommended: Railway or ngrok**

**Why:**
- ✅ Free
- ✅ Easy setup (< 10 minutes)
- ✅ Handles 32 concurrent users easily
- ✅ HTTPS included
- ✅ No server maintenance

**Steps:**
1. Deploy to Railway (permanent) OR use ngrok (temporary)
2. Share URL with all users
3. Provide credentials:
   - Admins: admin1, admin2
   - Users: user1-user30

### For Production/Long-term:

**Recommended: AWS or DigitalOcean**

**Why:**
- ✅ Full control
- ✅ Better performance
- ✅ Custom domain support
- ✅ Scalable

---

## 🚀 Quick Start - Recommended Path

### For Immediate Testing (5 minutes):

**Use ngrok:**
```bash
# Terminal 1:
cd stock-trading-app
mvn spring-boot:run

# Terminal 2:
ngrok http 8080

# Share the https URL with everyone!
```

### For Permanent Deployment (15 minutes):

**Use Railway:**
1. Push code to GitHub
2. Connect GitHub to Railway
3. Deploy automatically
4. Share Railway URL

Both methods give you a working link that 32+ users can access simultaneously!

---

## 📞 Support

If you encounter issues:
1. Check application logs
2. Verify firewall settings
3. Test local access first (localhost:8080)
4. Then test network access
5. Finally test public access

---

## ✅ Deployment Checklist

- [ ] Application runs locally (localhost:8080)
- [ ] Firewall configured
- [ ] IP address identified
- [ ] Network access tested
- [ ] Public deployment method chosen
- [ ] SSL/HTTPS configured (for public)
- [ ] Default passwords changed
- [ ] User accounts verified
- [ ] Multi-user testing completed
- [ ] Documentation shared with users

---

**Your application is now ready for multi-user access!** 🎉
