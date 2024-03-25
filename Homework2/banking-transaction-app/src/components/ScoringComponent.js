import React, { useState } from 'react';
import axios from 'axios';
import './ScoringComponent.css';

function ScoringComponent() {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [scoringResult, setScoringResult] = useState(null);
  const [error, setError] = useState('');

  const fetchTransactionsAndComputeScoring = async () => {
    try {
      const response = await axios.get(`http://ec2-16-16-217-128.eu-north-1.compute.amazonaws.com:8080/transactions/date?start=${startDate}&end=${endDate}`);
      const amounts = response.data.map(transaction => ({ amount: transaction.amount }));
      const scoringResponse = await axios.post('https://bbx2nt3nrg.execute-api.eu-north-1.amazonaws.com/dev/ScoringFunction', amounts);

      setScoringResult(scoringResponse.data);
      setError('');
    } catch (error) {
      console.error('Error fetching transactions and computing scoring:', error);
      setError('Failed to compute scoring.');
    }
  };

  return (
    <div className="scoring-container">
      <h2>Scoring Component</h2>
      <div className="input-group">
        <label htmlFor="startDate">Start Date:</label>
        <input type="date" id="startDate" value={startDate} onChange={e => setStartDate(e.target.value)} />
      </div>
      <div className="input-group">
        <label htmlFor="endDate">End Date:</label>
        <input type="date" id="endDate" value={endDate} onChange={e => setEndDate(e.target.value)} />
      </div>
      <button onClick={fetchTransactionsAndComputeScoring}>Compute Scoring</button>
      {error && <p className="error-message">{error}</p>}
      {scoringResult && (
        <div className="scoring-result">
          <h3>Scoring Result</h3>
          <p>Total Received: {scoringResult.totalReceived} RON</p>
          <p>Total Spent: {scoringResult.totalSpent} RON</p>
        </div>
      )}
    </div>
  );
}

export default ScoringComponent;
