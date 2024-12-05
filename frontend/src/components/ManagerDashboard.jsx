/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
import React, { useEffect} from 'react';
import { Link, useNavigate  } from 'react-router-dom';
import backgroundImage from '../assets/assets/image/food-background.jpg';
import axios from 'axios';



const ManagerDashboard = () => {
  const navigate = useNavigate();
  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const dashboardData = await axios.get('/api/managerDashboard');
        // Update state with dashboard data
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      }
    };

    const handleLogout = () => {
      localStorage.removeItem('token');
      sessionStorage.clear();
      navigate('/login');
    };
    
    
    fetchDashboardData();
  },
   []);
  return (
    <div style={{
      fontFamily: 'Arial, sans-serif',
      background: `url(${backgroundImage}) no-repeat center center fixed`,
      backgroundSize: 'cover',
      color: '#333',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh',
      margin: 0
    }}>
      <div style={{
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        padding: '2rem',
        maxWidth: '800px',
        width: '100%',
        borderRadius: '8px',
        boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)',
        textAlign: 'center'
      }}>
        <h1>Welcome to the Manager Dashboard</h1>
        <p className="welcome-message">Manage orders, review income that are bieng generated, and enhance learning with improvement in your restaurant.</p>

        <div className="dashboard-section">
          <h2> Management</h2>
          <p>Create, edit, and manage order to challenge and inspire your customer.</p>
          <Link to="/manager/create-menu" className="btn">Create New menus</Link>
          <Link to="/manager/view-menus" className="btn">Manage Existing menus</Link>
        </div>

        <div className="dashboard-section">
          <h2>restaurant Progress</h2>
          <p>Review orders and customer  performance and track their progress to provide personalized feedback.</p>
          <Link to="/manager/restaurant_performance" className="btn">View restaurant Progress</Link>
        </div>

        <div className="dashboard-section">
          <h2>Analytics</h2>
          <p>Gain insights into orders growth and identify areas where restaurrant need improvement.</p>
          <Link to="/manager/analytics" className="btn">View orders Analytics</Link>
        </div>

        <div className="dashboard-section">
          <h2> Resources</h2>
          <p>Access additional resources to support your customer and aid work_mate through give them some guide line .</p>
          <Link to="/resources" className="btn">View Resources</Link>

      
        </div>
        <button onClick= {handleLogout}>
          Logout
        </button>

      </div>

      <style>
        {`
          .dashboard-section {
            margin: 1.5rem 0;
            background-color: #f9f9f9;
            padding: 1rem;
            border-radius: 8px;
            text-align: left;
          }

          .btn {
            display: inline-block;
            margin: 0.5rem;
            padding: 10px 20px;
            background-color: #6a11cb;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
          }

          .btn:hover {
            background-color: #2575fc;
          }

          .logout a {
            color: #6a11cb;
            text-decoration: none;
          }

          .logout a:hover {
            text-decoration: underline;
          }
        `}
      </style>

    </div>
  );
};

export default ManagerDashboard;
