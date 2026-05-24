/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'https://syfe-finance-platform-adityazzzz.onrender.com/api/:path*',
      },
    ];
  },
};

module.exports = nextConfig;
