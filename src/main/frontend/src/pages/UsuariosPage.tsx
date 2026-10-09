import { useEffect, useState } from 'react'
import axios from 'axios'
import api from '../services/api'
import type { Usuario } from '../types/Usuario'
import UsuarioList from '../components/UsuarioList'
import UsuarioForm, { type UsuarioDados } from '../components/UsuarioForm'

export default function UsuariosPage() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([])
  const [editing, setEditing] = useState<Usuario | null>(null)
  const [revision, setRevision] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')

  useEffect(() => {
    const controller = new AbortController()
    setLoading(true)
    setError('')
    api.get<Usuario[]>('/usuarios', { signal: controller.signal })
      .then(({ data }) => setUsuarios(data))
      .catch((err: unknown) => {
        if (!axios.isCancel(err)) setError('Não foi possível carregar os usuários. Confira o back-end e seu login de administrador.')
      })
      .finally(() => { if (!controller.signal.aborted) setLoading(false) })
    return () => controller.abort()
  }, [revision])

  const salvar = async (dados: UsuarioDados): Promise<boolean> => {
    setError(''); setNotice('')
    try {
      if (editing) await api.put(`/usuarios/${editing.id}`, { ...dados, senha: dados.senha || null })
      else await api.post('/usuarios', { nome: dados.nome, username: dados.username, email: dados.email, senha: dados.senha })
      setEditing(null); setNotice('Usuário salvo.'); setRevision(v => v + 1)
      return true
    } catch (err) {
      setError(axios.isAxiosError(err) && err.response?.status === 409 ? 'Email ou username já cadastrado.' : 'Não foi possível salvar o usuário.')
      return false
    }
  }
  const excluir = async (usuario: Usuario) => {
    if (!window.confirm(`Excluir ${usuario.nome}?`)) return
    setError(''); setNotice('')
    try {
      await api.delete(`/usuarios/${usuario.id}`)
      if (editing?.id === usuario.id) setEditing(null)
      setNotice('Usuário excluído.'); setRevision(v => v + 1)
    } catch { setError('Não foi possível excluir o usuário.') }
  }

  return <>
    <section className="card"><h2>{editing ? 'Editar usuário' : 'Cadastrar usuário'}</h2>
      <UsuarioForm usuario={editing} onSubmit={salvar} onCancel={() => setEditing(null)} />
    </section>
    {notice && <p className="notice" role="status">{notice}</p>}
    {error && <p className="alert" role="alert">{error}</p>}
    {loading ? <p role="status">Carregando usuários...</p> : <section className="card"><h2>Lista de usuários</h2>
      {!error && usuarios.length === 0 && <p>Nenhum usuário cadastrado.</p>}
      <UsuarioList usuarios={usuarios} admin={true} onEdit={setEditing} onDelete={excluir} />
    </section>}
  </>
}
