# Trading Experiment Design Specification

## Overview
A structured 10-stock trading experiment where users make decisions across 100 trading days (10 stocks × 10 days each).

## Key Requirements

### 1. Fixed Sequential Structure
- **10 Stocks** in predetermined order
- **Progress Bar**: "Stock X of 10"
- Each stock = independent episode
- Fresh capital for each stock

### 2. Data Selection Algorithm
- Source: 200-day OHLCV history per stock
- Extract: 10 non-overlapping 10-day windows
- Criteria: High volatility segments
- Total: 100 decision points

### 3. Day-by-Day Progression
- Day 1 revealed → User action → Day 2 unlocked
- Actions: **Buy, Sell, Hold**
- Future data hidden until action taken
- Sequential unlock mechanism

### 4. Episode Flow
1. Initialize Stock N
2. Load 10-day segment
3. Reveal Day 1
4. User makes decision
5. System advances to Day 2
6. Repeat until Day 10
7. Show episode summary
8. Auto-load Stock N+1
9. Reset capital
10. Continue until Stock 10 complete

### 5. Completion & Logging
- Consolidate all 100 decisions
- Generate session summary
- Block portal after completion
- Permanent data storage

## Implementation Requirements

### Backend Components
1. Experiment Session Manager
2. Volatility Analyzer (select high-volatility windows)
3. Day-by-Day State Machine
4. Decision Logger
5. Session Summary Generator
6. Completion Blocker

### Frontend Components
1. Progress Bar (Stock X of 10)
2. Day Counter (Day X of 10)
3. Current Stock Display
4. Action Buttons (Buy/Sell/Hold)
5. Portfolio Summary (per episode)
6. Episode Transition Screen
7. Final Summary Dashboard

### Database Schema
```sql
-- Experiment Sessions
CREATE TABLE experiment_sessions (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    completed BOOLEAN,
    current_stock_index INTEGER,
    current_day INTEGER
);

-- Experiment Decisions
CREATE TABLE experiment_decisions (
    id BIGINT PRIMARY KEY,
    session_id BIGINT,
    stock_index INTEGER,
    day_number INTEGER,
    action VARCHAR(10), -- BUY, SELL, HOLD
    price DOUBLE,
    quantity INTEGER,
    capital_before DOUBLE,
    capital_after DOUBLE,
    timestamp TIMESTAMP
);

-- Experiment Stocks
CREATE TABLE experiment_stocks (
    id BIGINT PRIMARY KEY,
    sequence_order INTEGER,
    stock_symbol VARCHAR(20),
    segment_start_day INTEGER,
    segment_end_day INTEGER
);
```

## Volatility Selection Algorithm

```
For each stock's 200-day history:
1. Calculate daily volatility (std dev of returns)
2. Use sliding window of 10 days
3. Rank windows by average volatility
4. Select top 10 non-overlapping windows
5. Ensure minimum 5-day gap between windows
```

## User Flow

```
Start Experiment
    ↓
Stock 1, Day 1
    ↓
[View Chart] → [Buy/Sell/Hold] → Day 2
    ↓
Continue until Day 10
    ↓
Episode Summary
    ↓
Stock 2, Day 1 (Capital Reset)
    ↓
Repeat...
    ↓
Stock 10, Day 10
    ↓
Final Summary
    ↓
Portal Blocked
```

## Capital Management
- **Initial Capital**: ₹100,000 per stock
- **Reset**: Fresh capital for each stock
- **Carry Over**: None (each stock independent)
- **Final Metric**: Average performance across 10 stocks

## Decision Constraints
- **Hold**: No transaction, advance to next day
- **Buy**: Can only buy if sufficient capital
- **Sell**: Can only sell if holding shares
- **Quantity**: Fixed lot size (e.g., 10 shares)

## Progress Indicators
```
Header:
┌─────────────────────────────────────────┐
│ Trading Experiment                      │
│ Stock 3 of 10 | Day 5 of 10            │
│ Progress: ████████░░░░░░░░░░ 35%       │
└─────────────────────────────────────────┘
```

## Session Summary Format
```json
{
  "session_id": "SESSION_123",
  "user_id": 1,
  "start_time": "2024-02-16T10:00:00Z",
  "end_time": "2024-02-16T11:30:00Z",
  "total_decisions": 100,
  "stocks": [
    {
      "stock_index": 1,
      "symbol": "STOCK_1",
      "initial_capital": 100000,
      "final_capital": 105000,
      "return_percent": 5.0,
      "decisions": [
        {
          "day": 1,
          "action": "BUY",
          "price": 100.5,
          "quantity": 10,
          "capital": 99000
        }
      ]
    }
  ],
  "summary": {
    "total_return": 8.5,
    "best_stock": "STOCK_5",
    "worst_stock": "STOCK_2",
    "hold_count": 30,
    "buy_count": 35,
    "sell_count": 35
  }
}
```

## Implementation Priority
1. ✓ Volatility analyzer
2. ✓ Experiment session management
3. ✓ Day-by-day UI
4. ✓ Decision logging
5. ✓ Progress tracking
6. ✓ Episode summaries
7. ✓ Completion blocking
8. ✓ Admin view of experiment results
