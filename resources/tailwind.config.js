module.exports = {
  content: [
    './src/**/*',
    './resources/**/*',
  ],
  theme: {
    extend: {
      gridTemplateColumns: {
        '24': 'repeat(24, minmax(0, 1fr))',
        '16': 'repeat(16, minmax(0, 1fr))',
      },
      aspectRatio: {
        '63/88': '63 / 88',
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
  ],
}
