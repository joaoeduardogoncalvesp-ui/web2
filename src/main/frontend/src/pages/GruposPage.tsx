import { useEffect, useState } from 'react'
import axios from 'axios'
import api from '../services/api'
import type { Grupo } from '../types/Grupo'
import GrupoList from '../components/GrupoList'
import GrupoForm, { type GrupoDados } from '../components/GrupoForm'

export default function GruposPage({ admin }: { admin: boolean }) {
  const [grupos, setGrupos] = useState<Grupo[]>([])
  const [editing, setEditing] = useState<Grupo | null>(null)
  const [revision, setRevision] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  useEffect(() => {
    const controller = new AbortController()
    setLoading(true); setError('')
    api.get<Grupo[]>('/grupos', { signal: controller.signal })
      .then(({ data }) => setGrupos(data))
      .catch((err: unknown) => { if (!axios.isCancel(err)) setError('Não foi possível carregar as grupos. Verifique o back-end.') })
      .finally(() => { if (!controller.signal.aborted) setLoading(false) })
    return () => controller.abort()
  }, [revision])
  const salvar = async (dados: GrupoDados): Promise<boolean> => {
    setError(''); setNotice('')
    try {
      if (editing) await api.put(`/grupos/${editing.id}`, dados)
      else await api.post('/grupos', dados)
      setEditing(null); setNotice('Grupo salva.'); setRevision(v => v + 1)
      return true
    } catch (err) {
      setError(axios.isAxiosError(err) && err.response?.status === 409
        ? 'Já existe uma grupo com esse nome. Escolha outro nome.'
        : 'Não foi possível salvar a grupo.')
      return false
    }
  }
  const excluir = async (grupo: Grupo) => {
    if (!window.confirm(`Excluir ${grupo.nome}?`)) return
    try {
      await api.delete(`/grupos/${grupo.id}`)
      if (editing?.id === grupo.id) setEditing(null)
      setNotice('Grupo excluída.'); setError(''); setRevision(v => v + 1)
    } catch { setError('Não foi possível excluir a grupo.') }
  }
  return <>
    {admin && <section className="card"><h2>{editing ? 'Editar' : 'Cadastrar'} grupo</h2><GrupoForm grupo={editing} onSubmit={salvar} onCancel={() => setEditing(null)} /></section>}
    {notice && <p className="notice" role="status">{notice}</p>}
    {error && <p className="alert" role="alert">{error}</p>}
    {loading ? <p role="status">Carregando grupos...</p> : <section className="card"><h2>Grupos</h2>
      {!error && grupos.length === 0 && <p>Nenhuma grupo cadastrada.</p>}
      <GrupoList grupos={grupos} admin={admin} onEdit={setEditing} onDelete={excluir} />
    </section>}
  </>
}
