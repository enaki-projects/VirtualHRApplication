import dotenv from 'dotenv';

// Set the NODE_ENV to 'development' by default
process.env.NODE_ENV = process.env.NODE_ENV || 'development';

if (process.env.NODE_ENV == 'development'){
  const envFound = dotenv.config();
  if (envFound.error) {
    throw new Error("⚠ Couldn't find .env file");
  }
}


export default {
  port: parseInt(process.env.PORT || "3000", 10),
  adapterIP: "https://" + (process.env.ADAPTER_IP || "localhost") + ":5000/",
  avatarIP: "https://" + (process.env.AVATAR_IP || "localhost") + ":2011/",
  /* For winston logger */
  logs: {
    level: process.env.LOG_LEVEL || 'silly',
  },

  /* API configs */
  api: {
    prefix: '',
  },
};
