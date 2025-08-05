import React from 'react';
import './App.css';
import DonorRegistrationForm from './components/DonorRegistrationForm';
import OrganRequestDashboard from './components/OrganRequestDashboard';
import OrganAvailabilityComponent from './components/OrganAvailabilityComponent';

function App() {
  return (
    <div className="App">
      <DonorRegistrationForm />
      <div style={{ height: '2rem' }}></div>
      <OrganRequestDashboard />
      <div style={{ height: '2rem' }}></div>
      <OrganAvailabilityComponent />
    </div>
  );
}

export default App;
