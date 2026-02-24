const http = require('http');

const postData = JSON.stringify({
  phone: '13800000001',
  password: '123456'
});

const options = {
  hostname: 'localhost',
  port: 8080,
  path: '/api/user/login',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(postData)
  }
};

const req = http.request(options, (res) => {
  console.log(`状态码: ${res.statusCode}`);
  console.log(`响应头: ${JSON.stringify(res.headers)}`);
  res.setEncoding('utf8');
  res.on('data', (chunk) => {
    console.log(`响应体: ${chunk}`);
  });
  res.on('end', () => {
    console.log('响应结束');
  });
});

req.on('error', (e) => {
  console.error(`请求错误: ${e.message}`);
});

// 写入数据到请求体
req.write(postData);
req.end();