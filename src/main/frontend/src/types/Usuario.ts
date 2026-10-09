export interface Usuario {
  id: number
  nome: string
  username: string | null
  email: string
  papel: 'USER' | 'ADMIN'
}
