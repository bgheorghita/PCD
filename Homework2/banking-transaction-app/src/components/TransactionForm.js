import React, { useState } from 'react';
import axios from 'axios';
import './TransactionForm.css';

function TransactionForm() {
  const [transaction, setTransaction] = useState({
    description: '',
    date: '',
    amount: '',
    categoryId: '',
    keywordId: '',
    parentId: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTransaction({ ...transaction, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://ec2-16-16-217-128.eu-north-1.compute.amazonaws.com:8080/transactions', transaction);
      alert('Transaction saved successfully!');
      setTransaction({
        description: '',
        date: '',
        amount: '',
        categoryId: '',
        keywordId: '',
        parentId: ''
      });
    } catch (error) {
      console.error('Error saving transaction:', error);
      alert('Error saving transaction. Please try again later.');
    }
  };

  return (
    <div className="transaction-form-container">
      <h2>Save Transaction</h2>
      <form className="transaction-form" onSubmit={handleSubmit}>
        <label>
          Description:
          <input type="text" name="description" value={transaction.description} onChange={handleChange} />
        </label>
        <br />
        <label>
          Date:
          <input type="date" name="date" value={transaction.date} onChange={handleChange} />
        </label>
        <br />
        <label>
          Amount:
          <input type="number" name="amount" value={transaction.amount} onChange={handleChange} />
        </label>
        <br />
        <label>
          Category ID:
          <input type="number" name="categoryId" value={transaction.categoryId} onChange={handleChange} />
        </label>
        <br />
        <label>
          Keyword ID:
          <input type="number" name="keywordId" value={transaction.keywordId} onChange={handleChange} />
        </label>
        <br />
        <label>
          Parent ID:
          <input type="number" name="parentId" value={transaction.parentId} onChange={handleChange} />
        </label>
        <br />
        <button type="submit">Save Transaction</button>
      </form>
    </div>
  );
}

export default TransactionForm;
