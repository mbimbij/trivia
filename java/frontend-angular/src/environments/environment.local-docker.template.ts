export const environment = {
  backendUrl: `http://localhost:$PORT_LOCAL_DOCKER`,
  backendWebSocketUrl: `ws://localhost:$PORT_LOCAL_DOCKER/ws/gs-guide-websocket`,
  firebaseConfig: {
    apiKey: '$FIREBASE_API_KEY',
  }
};
