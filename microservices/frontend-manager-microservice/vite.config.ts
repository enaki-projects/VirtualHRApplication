import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Served by the gateway under /manager/ (same origin as /api/ and /avatar-api/).
export default defineConfig({
  base: '/manager/',
  plugins: [react()],
  build: {
    outDir: 'build',
    chunkSizeWarningLimit: 2000,
  },
  css: {
    preprocessorOptions: {
      scss: { quietDeps: true },
    },
  },
});
