import axios from 'axios'

const api = axios.create({ baseURL: '/api', timeout: 10000 })

api.interceptors.request.use((config) => {
  try {
    const session = JSON.parse(sessionStorage.getItem('session') || 'null')
    if (session?.token) config.headers.Authorization = `Bearer ${session.token}`
  } catch { /* sessão inválida: segue sem token */ }
  return config
})

export default api
