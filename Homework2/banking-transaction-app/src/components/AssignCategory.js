import React, { useState } from 'react';
import axios from 'axios';
import './AssignCategory.css';

function AssignCategory() {
  const [txId, setTxId] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [message, setMessage] = useState('');

  const assignCategory = async () => {
    try {
      const response = await axios.put(`https://e7xh1enmue.execute-api.eu-north-1.amazonaws.com/dev/manual-classifier?txId=${txId}&categoryId=${categoryId}`);
      setMessage(response.data);
    } catch (error) {
      console.error('Error assigning category:', error);
      setMessage('Failed to assign category.');
    }
  };

  const displayMessage = () => {
    if (message && typeof message === 'object') {
      const { id, description, date, amount, category } = message;
      return (
        <div>
          <p>ID: {id}</p>
          <p>Description: {description}</p>
          <p>Date: {new Date(date).toLocaleDateString()}</p>
          <p>Amount: {amount}</p>
          <p>Category: {category ? category.value : 'N/A'}</p>
        </div>
      );
    } else {
      return <p>{message}</p>;
    }
  };

  return (
    <div className="assign-category-container">
      <h2>Assign Category to Transaction</h2>
      <div className="input-group">
        <label htmlFor="txId">Transaction ID:</label>
        <input type="text" id="txId" value={txId} onChange={e => setTxId(e.target.value)} />
      </div>
      <div className="input-group">
        <label htmlFor="categoryId">Category ID:</label>
        <input type="text" id="categoryId" value={categoryId} onChange={e => setCategoryId(e.target.value)} />
      </div>
      <button className="assign-button" onClick={assignCategory}>Assign Category</button>
      <div className="message-container">
        {displayMessage()}
      </div>
    </div>
  );
}

export default AssignCategory;
