import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  base: './', // Ensure relative paths for assets

  server:{
    port:3000
  },
  resolve: {
    extensions: ['.js', '.jsx']
  },

  build: {
    rollupOptions: {
      external: ['react', 'react-dom'],
    },
    outDir: 'dist',

    output: {
      manualChunks: undefined // Default settings
    }
  },
});
