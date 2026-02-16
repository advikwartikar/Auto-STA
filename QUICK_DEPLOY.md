# 🚀 Quick Deployment Guide - 3 Easy Methods

Choose the method that fits your needs:

## ⚡ Method 1: ngrok (Fastest - 2 Minutes)

**Perfect for:** Quick demos, testing, temporary access

### Steps:

1. **Start your application:**
```bash
cd stock-trading-app
mvn spring-boot:run
```

2. **In a new terminal, install and run ngrok:**
```bash
# Install ngrok (Mac):
brew install ngrok

# Or download from: https://ngrok.com/download

# Run ngrok:
ngrok http 8080
```

3. **Copy and share the URL shown:**
```
https://abc123.ngrok-free.app
```

**Done!** Everyone can access this URL from anywhere.

**Note:** URL changes when you restart ngrok (free tier).

---

## 🌐 Method 2: Railway (Best - 10 Minutes)

**Perfect for:** Permanent deployment, 24/7 access, production use

### Steps:

1. **Create account:** https://railway.app (free, use GitHub login)

2. **Install Railway CLI:**
```bash
# Mac:
brew install railway

# Linux/Windows: Download from railway.app
```

3. **Login and deploy:**
```bash
cd stock-trading-app

# Login
railway login

# Initialize
railway init

# Deploy
railway up

# Get URL
railway domain
```

4. **Share the URL:**
```
https://stock-trading-app.railway.app
```

**Done!** App is live 24/7, auto-restarts, permanent URL!

**Bonus:** Railway auto-detects Spring Boot and configures everything!

---

## 📡 Method 3: Local Network (5 Minutes)

**Perfect for:** Same WiFi/LAN access, no internet needed

### Steps:

1. **Find your IP address:**
```bash
# Windows:
ipconfig

# Mac/Linux:
ifconfig | grep inet

# Or simply:
hostname -I
```
Your IP will look like: `192.168.1.100`

2. **Start application with network access:**
```bash
cd stock-trading-app
mvn spring-boot:run -Dspring-boot.run.arguments="--server.address=0.0.0.0"
```

3. **Share with devices on same WiFi:**
```
http://192.168.1.100:8080
(Replace with YOUR IP)
```

**Done!** Anyone on the same WiFi can access it.

---

## 🎯 Which Method Should You Choose?

| Need | Best Method | Time | Cost |
|------|-------------|------|------|
| Quick demo | ngrok | 2 min | Free |
| Permanent public access | Railway | 10 min | Free |
| Same WiFi access only | Local Network | 5 min | Free |
| Production deployment | Railway + Custom Domain | 15 min | Free* |

*Free tier: 500 hours/month (plenty for testing)

---

## 🔥 FASTEST PATH (Recommended for Dissertation)

### Use Railway - Here's why:

✅ **One command deployment**  
✅ **Permanent HTTPS URL**  
✅ **Auto-restarts if crashes**  
✅ **No server management**  
✅ **Free tier is generous**  
✅ **Works from anywhere**  

### Railway Quick Start:

```bash
# 1. Install Railway CLI
npm install -g @railway/cli

# 2. Login
railway login

# 3. Deploy (from project directory)
cd stock-trading-app
railway init
railway up

# 4. Get your URL
railway domain
```

**Share the URL and you're done!** 🎉

---

## 👥 For Your 2 Admins + 30 Users

### After Deployment:

1. **Share the URL** (e.g., https://your-app.railway.app)

2. **Share credentials:**
```
ADMINS:
Username: admin1 | Password: admin123
Username: admin2 | Password: admin123

USERS:
Username: user1-user30 | Password: user123
```

3. **All 32 users can access simultaneously!**

---

## 🆘 Quick Troubleshooting

### ngrok: "command not found"
```bash
# Install it first:
brew install ngrok
# or download from ngrok.com
```

### Railway: "not logged in"
```bash
railway login
# Opens browser for authentication
```

### Local Network: Can't connect from other device
```bash
# Check firewall:
# Windows: Allow Java in Windows Firewall
# Mac: System Preferences > Security > Firewall > Allow Java
# Linux: sudo ufw allow 8080
```

---

## 📱 Test Your Deployment

1. **Open URL on your phone** (different device)
2. **Login as user1 / user123**
3. **Try buying a stock**
4. **Check it works!**

If you can do this, everyone else can too! ✅

---

## 🎓 For Your Dissertation

**I recommend Railway because:**
- ✅ Permanent URL (include in your report)
- ✅ Works 24/7 (evaluators can test anytime)
- ✅ HTTPS (secure, professional)
- ✅ Auto-scaling (handles multiple users)
- ✅ No maintenance needed

**Setup time:** 10 minutes  
**Cost:** $0 (free tier covers your needs)  
**Reliability:** 99.9% uptime

---

Need help? The full guide has detailed troubleshooting!
