export const environment = {
  backendUrl: `http://localhost:$PORT_LOCAL_IDE_BACKEND`,
  backendWebSocketUrl: `ws://localhost:$PORT_LOCAL_IDE_BACKEND/ws/gs-guide-websocket`,
  firebaseConfig: {
    apiKey: '$FIREBASE_API_KEY',
  }
};
