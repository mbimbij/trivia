// load-env.js
const fs = require('fs');
const dotenv = require('dotenv');

// Load environment variables from .env file
dotenv.config();

// Create an environment file in the Angular environment folder
const targetPath = './src/environments/environment.ts';

// Write the environment variables to the environment file
const envConfigFile = `
export const environment = {
  production: ${process.env.RUN_ENVIRONMENT === 'production'},
  backendUrl: '${process.env.BACKEND_URL ?? 'http://localhost:8080'}',
  firebaseConfig: {
    apiKey: '${process.env.FIREBASE_API_KEY}',
  }
};
`;

fs.writeFile(targetPath, envConfigFile, (err) => {
  if (err) {
    console.error(err);
  } else {
    console.log(`Environment file generated at ${targetPath}`);
  }
});
