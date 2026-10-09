import { useEffect, useState, type FormEvent } from 'react'
import type { Usuario } from '../types/Usuario'

export interface UsuarioDados {
  nome: string
  username: string
  email: string
  senha: string
  papel: 'USER' | 'ADMIN'
}

interface Props {
  usuario: Usuario | null
  onSubmit: (dados: UsuarioDados) => Promise<boolean>
  onCancel: () => void
}

const vazio: UsuarioDados = { nome: '', username: '', email: '', senha: '', papel: 'USER' }

export default function UsuarioForm({ usuario, onSubmit, onCancel }: Props) {
  const [dados, setDados] = useState<UsuarioDados>(vazio)
  const [saving, setSaving] = useState(false)
  useEffect(() => setDados(usuario ? { nome: usuario.nome, username: usuario.username || '', email: usuario.email, senha: '', papel: usuario.papel } : vazio), [usuario])

  const submit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setSaving(true)
    try { const saved = await onSubmit(dados); if (saved && !usuario) setDados(vazio) }
    finally { setSaving(false) }
  }

  return <form onSubmit={submit}>
    <label>Nome<input required value={dados.nome} onChange={e => setDados({ ...dados, nome: e.target.value })} /></label>
    <label>Username<input required value={dados.username} onChange={e => setDados({ ...dados, username: e.target.value })} /></label>
    <label>Email<input required type="email" value={dados.email} onChange={e => setDados({ ...dados, email: e.target.value })} /></label>
    <label>Senha {usuario && '(deixe vazia para manter)'}<input type="password" required={!usuario} minLength={8} value={dados.senha} onChange={e => setDados({ ...dados, senha: e.target.value })} /></label>
    {usuario && <label>Perfil<select value={dados.papel} onChange={e => setDados({ ...dados, papel: e.target.value as UsuarioDados['papel'] })}><option value="USER">Usuário</option><option value="ADMIN">Administrador</option></select></label>}
    <div className="actions"><button type="submit" disabled={saving}>{saving ? 'Salvando...' : 'Salvar'}</button>{usuario && <button type="button" onClick={onCancel}>Cancelar</button>}</div>
  </form>
}
