import dotenv from 'dotenv';

process.env.NODE_ENV = process.env.NODE_ENV || 'development';

if (process.env.NODE_ENV === 'development') {
  // Optional in development; ignore a missing .env file.
  dotenv.config();
}

function normaliseBasePath(value: string | undefined): string {
  const trimmed = (value || '').trim().replace(/\/+$/, '');
  if (!trimmed) return '';
  return trimmed.startsWith('/') ? trimmed : '/' + trimmed;
}

function withTrailingSlash(value: string): string {
  return value.endsWith('/') ? value : value + '/';
}

const sessionSecret = process.env.SESSION_SECRET;
if (!sessionSecret && process.env.NODE_ENV === 'production') {
  throw new Error('SESSION_SECRET must be set in production');
}

export default {
  port: parseInt(process.env.PORT || '3000', 10),
  /* Mount point when served behind the gateway, e.g. "/admin". Empty = root. */
  basePath: normaliseBasePath(process.env.BASE_PATH),
  /* Server-side base URL of cv-adapter (internal network). */
  adapterUrl: withTrailingSlash(process.env.ADAPTER_URL || 'http://localhost:5000/'),
  /* Browser-facing base URL of cv-adapter (same origin behind the gateway). */
  publicAdapterUrl: withTrailingSlash(process.env.PUBLIC_ADAPTER_URL || '/'),
  sessionSecret: sessionSecret || 'dev-only-change-me',
  logs: {
    level: process.env.LOG_LEVEL || 'info',
  },
};
