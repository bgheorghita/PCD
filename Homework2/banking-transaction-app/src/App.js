import React from 'react';
import TransactionForm from './components/TransactionForm';
import CategoryList from './components/CategoryList';
import TransactionList from './components/TransactionList';
import AssignCategory from './components/AssignCategory';
import TransactionByDateRange from './components/TransactionByDateRange';
import ScoringComponent from './components/ScoringComponent';
import NotificationComponent from './components/NotificationComponent';

function App() {
  return (
    <div className="App">
      <TransactionForm />
      <TransactionList />
      <CategoryList />
      <AssignCategory />
      <TransactionByDateRange />
      <ScoringComponent />
      <NotificationComponent />
    </div>
  );
}

export default App;
