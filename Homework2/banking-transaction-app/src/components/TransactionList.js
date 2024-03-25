import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './TransactionList.css';

function TransactionList() {
  const [transactions, setTransactions] = useState([]);
  const [fetching, setFetching] = useState(false);

  useEffect(() => {
    if (fetching) {
      fetchTransactions();
    }
  }, [fetching]);

  const fetchTransactions = async () => {
    try {
      const response = await axios.get('http://ec2-16-16-217-128.eu-north-1.compute.amazonaws.com:8080/transactions');
      setTransactions(response.data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const handleToggleTransactions = () => {
    if (fetching) {
      setTransactions([]);
    }
    setFetching(prevFetching => !prevFetching);
  };

  return (
    <div className="transaction-list-container">
      <button onClick={handleToggleTransactions} className="toggle-button">
        {fetching ? 'Hide Transactions' : 'See Transactions'}
      </button>
      {fetching && transactions.length > 0 && (
        <div className="table-container">
          <h2>Transaction Table</h2>
          <table className="transaction-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Date</th>
                <th>Amount</th>
                <th>Category ID</th>
                <th>Keyword ID</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map(transaction => (
                <tr key={transaction.id}>
                  <td>{transaction.id}</td>
                  <td>{transaction.description}</td>
                  <td>{transaction.date}</td>
                  <td>{transaction.amount}</td>
                  <td>{transaction.category ? transaction.category.id : ''}</td>
                  <td>{transaction.keyword ? transaction.keyword.id : ''}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default TransactionList;
