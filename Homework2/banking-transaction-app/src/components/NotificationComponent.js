import React, { useEffect, useState } from 'react';


function WebSocketComponent() {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const socket = new WebSocket('wss://5zf4z4w57l.execute-api.eu-central-1.amazonaws.com/production/');
    socket.addEventListener('open', () => console.log('WebSocket Connection Established'));

    socket.addEventListener('message', (event) => {
      const data = JSON.parse(event.data);
      setMessages((prevMessages) => [...prevMessages, data]);
    });

    return () => socket.close();
  }, []);

  return (
    <div className="scoring-container">
      <h2>Notification Component</h2>
      {messages.map((message, index) => (
        <p key={`notification-${index}`}>{message}</p>
      ))}
    </div>

  );
}


export default WebSocketComponent;
