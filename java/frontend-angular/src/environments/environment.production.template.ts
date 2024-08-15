export const environment = {
  backendUrl: `https://$HOSTNAME_PROD`,
  backendWebSocketUrl: `wss://$HOSTNAME_PROD/ws/gs-guide-websocket`,
  firebaseConfig: {
    apiKey: '$FIREBASE_API_KEY',
  }
};
