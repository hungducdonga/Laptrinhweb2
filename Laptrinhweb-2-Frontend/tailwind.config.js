/** @type {import('tailwindcss').Config} */
export default {
  darkMode: ['class'],
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    screens: {
      ssm: '368px',

      sm: '576px',

      md: '768px',

      lg: '992px',

      xl: '1200px',

      '2xl': '1440px'
    },
    extend: {},
  },
  plugins: [],
}

