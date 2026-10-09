import { useEffect, useState } from 'react'
import axios from 'axios'
import api from '../services/api'
import type { Permissao } from '../types/Permissao'
import PermissaoList from '../components/PermissaoList'
import PermissaoForm, { type PermissaoDados } from '../components/PermissaoForm'

export default function PermissoesPage({ admin }: { admin: boolean }) {
  const [permissoes, setPermissoes] = useState<Permissao[]>([])
  const [editing, setEditing] = useState<Permissao | null>(null)
  const [revision, setRevision] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  useEffect(() => {
    const controller = new AbortController()
    setLoading(true); setError('')
    api.get<Permissao[]>('/permissoes', { signal: controller.signal })
      .then(({ data }) => setPermissoes(data))
      .catch((err: unknown) => { if (!axios.isCancel(err)) setError('Não foi possível carregar as permissões. Verifique o back-end.') })
      .finally(() => { if (!controller.signal.aborted) setLoading(false) })
    return () => controller.abort()
  }, [revision])
  const salvar = async (dados: PermissaoDados): Promise<boolean> => {
    setError(''); setNotice('')
    try {
      if (editing) await api.put(`/permissoes/${editing.id}`, dados)
      else await api.post('/permissoes', dados)
      setEditing(null); setNotice('Permissão salva.'); setRevision(v => v + 1)
      return true
    } catch { setError('Não foi possível salvar a permissão. Confira se o nome já está cadastrado.'); return false }
  }
  const excluir = async (permissao: Permissao) => {
    if (!window.confirm(`Excluir ${permissao.nome}?`)) return
    try {
      await api.delete(`/permissoes/${permissao.id}`)
      if (editing?.id === permissao.id) setEditing(null)
      setNotice('Permissão excluída.'); setError(''); setRevision(v => v + 1)
    } catch { setError('Não foi possível excluir a permissão.') }
  }
  return <>
    {admin && <section className="card"><h2>{editing ? 'Editar' : 'Cadastrar'} permissão</h2><PermissaoForm permissao={editing} onSubmit={salvar} onCancel={() => setEditing(null)} /></section>}
    {notice && <p className="notice" role="status">{notice}</p>}
    {error && <p className="alert" role="alert">{error}</p>}
    {loading ? <p role="status">Carregando permissões...</p> : <section className="card"><h2>Permissões</h2>
      {!error && permissoes.length === 0 && <p>Nenhuma permissão cadastrada.</p>}
      <PermissaoList permissoes={permissoes} admin={admin} onEdit={setEditing} onDelete={excluir} />
    </section>}
  </>
}
