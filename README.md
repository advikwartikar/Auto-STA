# Stock Trading Simulation Application

A comprehensive Java Spring Boot application for stock market trading simulation with virtual credits.

## Features

- **User Management**: 2 Admin users and 30 Regular users
- **Virtual Trading**: Users get 100,000 virtual credits to trade stocks
- **Real Stock Data**: CSV-based OHLCV data with technical indicators (SMA, RSI)
- **Portfolio Management**: Track holdings, profit/loss in real-time
- **Interactive Charts**: Candlestick and line charts for stock analysis
- **Admin Dashboard**: Admins can view all user portfolios and transactions
- **Secure**: Spring Security with role-based access control

## Technology Stack

- **Backend**: Spring Boot 3.2, Spring Security, Spring Data JPA
- **Database**: H2 (embedded) / MySQL
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript, Chart.js
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Optional) MySQL 8.0+ if using MySQL instead of H2

## Quick Start

### 1. Build the Application

```bash
cd stock-trading-app
mvn clean install
```

### 2. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/stock-trading-app-1.0.0.jar
```

### 3. Access the Application

- **Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:file:./data/stocktrading
  - Username: sa
  - Password: (leave blank)

## Default Credentials

### Admin Users
- Username: admin1 / Password: admin123
- Username: admin2 / Password: admin123

### Regular Users
- Username: user1 to user30 / Password: user123

## Database Configuration

### Using H2 (Default - Embedded)
No additional configuration needed. Data is stored in `./data/stocktrading.mv.db`

### Using MySQL

1. Create database:
```sql
CREATE DATABASE stocktrading;
```

2. Update `application.properties`:
```properties
# Comment out H2 config
# Uncomment MySQL config
spring.datasource.url=jdbc:mysql://localhost:3306/stocktrading?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## Application Structure

```
src/
├── main/
│   ├── java/com/stocktrading/
│   │   ├── model/          # Entity classes (User, Stock, Transaction, Portfolio)
│   │   ├── repository/     # JPA repositories
│   │   ├── service/        # Business logic
│   │   ├── controller/     # REST & Web controllers
│   │   ├── config/         # Security & app configuration
│   │   └── dto/            # Data Transfer Objects
│   └── resources/
│       ├── application.properties
│       ├── data/           # CSV stock data files
│       ├── static/         # CSS, JS, images
│       └── templates/      # Thymeleaf templates
```

## Features Explained

### For Users:
1. **Dashboard**: View portfolio summary, available credits, profit/loss
2. **Browse Stocks**: See all available stocks with current prices and indicators
3. **Buy Stocks**: Purchase stocks using virtual credits
4. **Sell Stocks**: Sell owned stocks
5. **View Charts**: Interactive candlestick and line charts
6. **Transaction History**: See all past buy/sell transactions

### For Admins:
1. **All User Features** plus:
2. **User Management**: View list of all users
3. **System Overview**: Total users, stocks, transactions
4. **User Portfolios**: View any user's portfolio and performance
5. **Transaction Monitoring**: See all transactions across all users

## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Authenticate user
- `GET /logout` - Logout

### User Endpoints
- `GET /dashboard` - User dashboard
- `GET /stocks` - List all stocks
- `GET /stocks/{id}` - Stock details with chart
- `POST /trade/buy` - Buy stocks
- `POST /trade/sell` - Sell stocks
- `GET /portfolio` - User's portfolio
- `GET /transactions` - User's transaction history

### Admin Endpoints
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/users` - List all users
- `GET /admin/user/{id}` - Specific user details
- `GET /admin/transactions` - All transactions

## Adding Stock Data

Place CSV files in `src/main/resources/data/` with format:
```csv
Open,High,Low,Close,Volume,SMA,RSI
100.0,102.5,99.5,101.0,50000,100.5,65.0
...
```

Files should be named: `stock_1.csv`, `stock_2.csv`, etc.

## Troubleshooting

### Port Already in Use
Change port in `application.properties`:
```properties
server.port=8081
```

### Database Connection Issues
- H2: Check file permissions in `./data/` directory
- MySQL: Verify MySQL service is running and credentials are correct

### CSV Loading Issues
- Ensure CSV files are in `src/main/resources/data/`
- Check CSV format matches expected headers
- Review application logs for specific errors

## Development

### Adding New Features
1. Create entity in `model/`
2. Create repository in `repository/`
3. Implement business logic in `service/`
4. Create controller in `controller/`
5. Add frontend templates in `templates/`

### Running Tests
```bash
mvn test
```

## Production Deployment

1. Build JAR:
```bash
mvn clean package
```

2. Run with production profile:
```bash
java -jar target/stock-trading-app-1.0.0.jar --spring.profiles.active=prod
```

3. Use external MySQL database
4. Configure proper security settings
5. Set up reverse proxy (nginx/Apache)

## License

Educational/Academic Use

## Support

For issues or questions, refer to the dissertation project documentation.

---

## 🌐 DEPLOYMENT - Make It Live!

### Quick Deployment Options:

**Option 1: ngrok (2 minutes - Temporary)**
```bash
# Start app
mvn spring-boot:run

# In new terminal
./deploy-ngrok.sh
```

**Option 2: Railway (10 minutes - Permanent)**
```bash
./deploy-railway.sh
```

**Option 3: Local Network (5 minutes - Same WiFi)**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.address=0.0.0.0"
# Access at: http://YOUR_IP:8080
```

📚 **Full deployment guide:** See `NETWORK_DEPLOYMENT_GUIDE.md` and `QUICK_DEPLOY.md`

---
