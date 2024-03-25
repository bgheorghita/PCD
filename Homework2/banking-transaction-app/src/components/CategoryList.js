import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './CategoryList.css'; // Import the CSS file for styling

function CategoryList() {
  const [categories, setCategories] = useState([]);
  const [showCategories, setShowCategories] = useState(false);

  useEffect(() => {
    if (showCategories) {
      fetchCategories();
    }
  }, [showCategories]);

  const fetchCategories = async () => {
    try {
      const response = await axios.get('http://ec2-16-16-217-128.eu-north-1.compute.amazonaws.com:8080/categories');
      setCategories(response.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  return (
    <div className="category-list-container">
      <button onClick={() => setShowCategories(!showCategories)}>
        {showCategories ? 'Hide Categories' : 'See Categories'}
      </button>
      {showCategories && (
        <table className="category-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Category Name</th>
            </tr>
          </thead>
          <tbody>
            {categories.map(category => (
              <tr key={category.id}>
                <td>{category.id}</td>
                <td>{category.value}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default CategoryList;
