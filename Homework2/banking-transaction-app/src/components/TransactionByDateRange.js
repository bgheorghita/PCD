import React, { useState } from 'react';
import axios from 'axios';
import './TransactionByDateRange.css';

function TransactionByDateRange() {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState('');
  const [showTable, setShowTable] = useState(false);

  const fetchTransactionsByDateRange = async () => {
    try {
      const response = await axios.get(`http://ec2-16-16-217-128.eu-north-1.compute.amazonaws.com:8080/transactions/date?start=${startDate}&end=${endDate}`);
      setTransactions(response.data);
      setError('');
      setShowTable(true);
    } catch (error) {
      console.error('Error fetching transactions by date range:', error);
      setError('Failed to fetch transactions.');
    }
  };

  const toggleTableVisibility = () => {
    setShowTable(prevState => !prevState);
  };

  return (
    <div className="transaction-by-date-range-container">
      <h2>Transactions by Date Range</h2>
      <div className="input-group">
        <label htmlFor="startDate">Start Date:</label>
        <input type="date" id="startDate" value={startDate} onChange={e => setStartDate(e.target.value)} />
      </div>
      <div className="input-group">
        <label htmlFor="endDate">End Date:</label>
        <input type="date" id="endDate" value={endDate} onChange={e => setEndDate(e.target.value)} />
      </div>
      <button onClick={fetchTransactionsByDateRange}>Fetch Transactions</button>
      <button onClick={toggleTableVisibility}>{showTable ? 'Hide Table' : 'Show Table'}</button>
      {error && <p className="error-message">{error}</p>}
      {showTable && transactions.length > 0 && (
        <div>
          <h3>Transactions</h3>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Date</th>
                <th>Amount</th>
                <th>Category ID</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map(transaction => (
                <tr key={transaction.id}>
                  <td>{transaction.id}</td>
                  <td>{transaction.description}</td>
                  <td>{transaction.date}</td>
                  <td>{transaction.amount}</td>
                  <td>{transaction.category ? transaction.category.id : 'N/A'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default TransactionByDateRange;
