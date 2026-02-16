# Experimental Trading Mode - Implementation Status

## ✅ COMPLETED COMPONENTS

### 1. Database Entities
- **ExperimentSession**: Tracks user progress through 10 stocks
- **ExperimentDecision**: Logs each Buy/Sell/Hold action
- **ExperimentStock**: Stores the 10 selected stocks and their volatile segments

### 2. Core Services

#### ExperimentService
**Functions:**
- `startExperiment()` - Initializes new session for user
- `getCurrentSession()` - Retrieves active session
- `isSessionExpired()` - Checks 2.5 hour time limit
- `makeDecision()` - Processes Buy/Sell/Hold actions
- `getEpisodeSummary()` - Generates per-stock summary
- `getSessionSummary()` - Generates complete 100-decision summary

**Key Logic:**
- ✓ 2.5 hour (150 minute) time limit
- ✓ 10 shares per trade (fixed lot size)
- ✓ ₹100,000 initial capital per stock
- ✓ Capital resets between stocks
- ✓ Automatic liquidation at end of each stock
- ✓ Progress tracking (X of 10 stocks, Y of 10 days)
- ✓ Session blocking after completion

#### VolatilityAnalyzer
**Functions:**
- `analyzeTop10Stocks()` - Selects 10 most volatile stocks from 110
- `analyzeStock()` - Finds 10 high-volatility 10-day windows per stock
- `calculateVolatility()` - Computes standard deviation of returns
- `windowsOverlap()` - Ensures 5-day gap between windows

**Selection Criteria:**
- ✓ Analyzes all 110 stocks
- ✓ Ranks by average volatility
- ✓ Selects top 10
- ✓ For each stock: 10 non-overlapping 10-day windows
- ✓ Minimum 5-day gap between windows
- ✓ Windows sorted by volatility

## ⏳ PENDING COMPONENTS

### 3. Data Initialization
**Need to create:** ExperimentDataLoader.java
- Run volatility analysis on startup
- Store top 10 stocks in experiment_stocks table
- Initialize sequence order (0-9)

### 4. Controllers
**Need to create:**
- `ExperimentController` with endpoints:
  - `GET /experiment/start` - Start new session
  - `GET /experiment/current` - Get current state
  - `POST /experiment/decision` - Make Buy/Sell/Hold decision
  - `GET /experiment/summary` - View final summary
  - `GET /experiment/stock-data` - Get current day's data

### 5. Frontend UI
**Need to create templates:**
- `experiment-dashboard.html` - Main experiment interface
  - Progress bar (Stock X of 10)
  - Day counter (Day Y of 10)
  - Time remaining display
  - Current price chart
  - Action buttons (Buy/Sell/Hold)
  - Capital & shares display
  
- `experiment-episode-summary.html` - Between-stock summary
  - Episode performance
  - Decision breakdown
  - Continue button
  
- `experiment-final-summary.html` - Final results
  - All 10 stocks performance
  - Average return
  - Action statistics
  - Download results button

### 6. Admin Features
**Need to create:**
- View all completed experiments
- Export experiment data (CSV/JSON)
- Participant statistics dashboard

## 📊 SYSTEM ARCHITECTURE

```
User Login
    ↓
Start Experiment Button
    ↓
ExperimentService.startExperiment()
    ↓
Load Stock 0, Day 0
    ↓
Show Chart + Action Buttons
    ↓
User Clicks Buy/Sell/Hold
    ↓
ExperimentService.makeDecision()
    ↓
Update Capital & Shares
    ↓
Advance to Next Day
    ↓
If Day 10: Episode Summary → Next Stock
    ↓
If Stock 10: Final Summary → Block Access
```

## 🔢 KEY PARAMETERS

| Parameter | Value |
|-----------|-------|
| Total Stocks | 10 (auto-selected from 110) |
| Days per Stock | 10 |
| Total Decisions | 100 |
| Initial Capital | ₹100,000 per stock |
| Shares per Trade | 10 (fixed) |
| Time Limit | 2.5 hours (150 minutes) |
| Window Gap | 5 days minimum |

## 🎯 EXPERIMENT FLOW

### Stock Episode (Repeated 10 times)
1. **Initialize**
   - Capital: ₹100,000
   - Shares: 0
   - Day: 0

2. **Day Loop** (Repeat 10 times)
   - Show current day's OHLCV data
   - Display: Capital, Shares, Current Price
   - Enable: Buy (if capital sufficient) / Sell (if shares available) / Hold
   - Log decision
   - Advance to next day

3. **Episode End**
   - Liquidate remaining shares
   - Calculate return %
   - Show summary
   - Reset capital
   - Move to next stock

### Session End
- After Stock 10, Day 10
- Generate comprehensive summary
- Save to database
- Block further access
- Show final results

## 📝 DATABASE SCHEMA

```sql
experiment_sessions:
- id (PK)
- user_id (FK)
- start_time
- end_time
- completed
- current_stock_index (0-9)
- current_day (0-9)
- current_capital
- current_shares

experiment_decisions:
- id (PK)
- session_id (FK)
- stock_index (0-9)
- day_number (0-9)
- action (BUY/SELL/HOLD)
- price
- quantity
- capital_before
- capital_after
- shares_before
- shares_after
- timestamp

experiment_stocks:
- id (PK)
- sequence_order (0-9)
- stock_symbol
- segment_start_day
- segment_end_day
- csv_file_path
```

## 🚀 TO COMPLETE THE IMPLEMENTATION

### Priority 1: Data Initialization (Critical)
Create ExperimentDataLoader to:
1. Run VolatilityAnalyzer on startup
2. Store top 10 stocks in database
3. Log selected stocks and windows

### Priority 2: Controllers (Required)
Create ExperimentController with all endpoints

### Priority 3: Frontend (Required)
Create 3 main UI pages:
- Experiment dashboard (trading interface)
- Episode summary (between stocks)
- Final summary (end results)

### Priority 4: Integration
Update DashboardController to show:
- "Start Experiment" button if no active session
- "Continue Experiment" if session in progress
- "View Results" if session completed

### Priority 5: Admin View
Add admin page to view all experiment results

## 💡 IMPLEMENTATION ESTIMATES

- Data Initialization: 30 minutes
- Controllers: 1 hour
- Frontend (3 pages): 2 hours
- Integration & Testing: 1 hour
- **Total: ~4.5 hours of development**

## ⚠️ CRITICAL NOTES

1. **Time Limit Enforcement**
   - Check on every decision
   - Auto-complete session if exceeded
   - Display countdown timer

2. **Data Hiding**
   - Only show current day's data
   - Future days hidden until unlocked
   - No "peek ahead" functionality

3. **Action Constraints**
   - Buy: Requires capital ≥ (price × 10)
   - Sell: Requires shares ≥ 10
   - Hold: Always available

4. **Capital Management**
   - Resets to ₹100,000 for each stock
   - No carry-over between stocks
   - Auto-liquidate at episode end

5. **Session Blocking**
   - One session per user
   - Cannot restart after completion
   - Admin can reset if needed

## 🧪 TESTING CHECKLIST

- [ ] Volatility analysis selects 10 stocks
- [ ] Each stock has 10 non-overlapping windows
- [ ] Session starts with Stock 0, Day 0
- [ ] Buy action deducts capital correctly
- [ ] Sell action adds capital correctly
- [ ] Hold action advances day
- [ ] Cannot buy without sufficient capital
- [ ] Cannot sell without sufficient shares
- [ ] Day advances after each action
- [ ] Stock changes after Day 10
- [ ] Capital resets between stocks
- [ ] Session completes after Stock 10
- [ ] Time limit enforced (2.5 hours)
- [ ] Session blocks after completion
- [ ] Summary generated correctly
- [ ] Progress bar updates correctly

## 📄 FILES CREATED

✓ model/ExperimentSession.java
✓ model/ExperimentDecision.java
✓ model/ExperimentStock.java
✓ repository/ExperimentSessionRepository.java
✓ repository/ExperimentDecisionRepository.java
✓ repository/ExperimentStockRepository.java
✓ service/ExperimentService.java
✓ service/VolatilityAnalyzer.java

## 📄 FILES NEEDED

⏳ config/ExperimentDataLoader.java
⏳ controller/ExperimentController.java
⏳ templates/experiment-dashboard.html
⏳ templates/experiment-episode-summary.html
⏳ templates/experiment-final-summary.html
⏳ templates/admin/experiment-results.html

---

**Status**: Core backend logic complete. Frontend and integration pending.
**Next Step**: Complete the remaining components to make system functional.
