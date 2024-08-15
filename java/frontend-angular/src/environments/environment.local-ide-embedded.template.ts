export const environment = {
  backendUrl: `http://localhost:$PORT_LOCAL_IDE_EMBEDDED`,
  backendWebSocketUrl: `ws://localhost:$PORT_LOCAL_IDE_EMBEDDED/ws/gs-guide-websocket`,
  firebaseConfig: {
    apiKey: '$FIREBASE_API_KEY',
  }
};
