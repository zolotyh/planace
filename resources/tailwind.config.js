const colors = require('tailwindcss/colors')

module.exports = {
  mode: "jit",
  content: ["./resources/**/*.html", "./resources/**/*.js", "./src/**/*"],
  theme: {
    fontFamily: {
      body: [
        "open_sanscondensed_light",
        "ui-sans-serif",
        "system-ui",
        "-apple-system",
        "BlinkMacSystemFont",
        "Segoe UI",
        "Roboto",
        "Helvetica Neue",
        "Arial",
        "Noto Sans",
        "sans-serif",
        "Apple Color Emoji",
        "Segoe UI Emoji",
        "Segoe UI Symbol",
        "Noto Color Emoji",
      ],
      georgia: ["Georgia", "Times", "Times New Roman", "serif"],
    },
    colors: {
      green: {
        DEFAULT: "#035E4C",
        900: "#103128"
      },
      darkgreen: "#103128",
      red: "#B6433C",
      originalRed: colors.red,
      yellow: "#EEE758",
      white: "#ffffff",
      gray: "#F5F5F5",
      black: "#2C363F",
      slate: colors.slate,
    },
    extend: {
      aspectRatio: {
        card: "98 / 136",
      },
      gridTemplateColumns: {
        // 24 column grid
        18: "repeat(18, minmax(0, 1fr))",
      },
    },
  },
  variants: {},
  plugins: [require("@tailwindcss/forms")],
};
