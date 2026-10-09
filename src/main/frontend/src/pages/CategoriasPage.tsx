import { useEffect, useState } from 'react'
import axios from 'axios'
import api from '../services/api'
import type { Categoria } from '../types/Categoria'
import CategoriaList from '../components/CategoriaList'
import CategoriaForm, { type CategoriaDados } from '../components/CategoriaForm'

export default function CategoriasPage({ admin }: { admin: boolean }) {
  const [categorias, setCategorias] = useState<Categoria[]>([])
  const [editing, setEditing] = useState<Categoria | null>(null)
  const [revision, setRevision] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')
  useEffect(() => {
    const controller = new AbortController()
    setLoading(true); setError('')
    api.get<Categoria[]>('/categorias', { signal: controller.signal })
      .then(({ data }) => setCategorias(data))
      .catch((err: unknown) => { if (!axios.isCancel(err)) setError('Não foi possível carregar as categorias. Verifique o back-end.') })
      .finally(() => { if (!controller.signal.aborted) setLoading(false) })
    return () => controller.abort()
  }, [revision])
  const salvar = async (dados: CategoriaDados): Promise<boolean> => {
    setError(''); setNotice('')
    try {
      if (editing) await api.put(`/categorias/${editing.id}`, dados)
      else await api.post('/categorias', dados)
      setEditing(null); setNotice('Categoria salva.'); setRevision(v => v + 1)
      return true
    } catch (err) {
      setError(axios.isAxiosError(err) && err.response?.status === 409
        ? 'Já existe uma categoria com esse nome. Escolha outro nome.'
        : 'Não foi possível salvar a categoria.')
      return false
    }
  }
  const excluir = async (categoria: Categoria) => {
    if (!window.confirm(`Excluir ${categoria.nome}?`)) return
    try {
      await api.delete(`/categorias/${categoria.id}`)
      if (editing?.id === categoria.id) setEditing(null)
      setNotice('Categoria excluída.'); setError(''); setRevision(v => v + 1)
    } catch { setError('Não foi possível excluir a categoria.') }
  }
  return <>
    {admin && <section className="card"><h2>{editing ? 'Editar' : 'Cadastrar'} categoria</h2><CategoriaForm categoria={editing} onSubmit={salvar} onCancel={() => setEditing(null)} /></section>}
    {notice && <p className="notice" role="status">{notice}</p>}
    {error && <p className="alert" role="alert">{error}</p>}
    {loading ? <p role="status">Carregando categorias...</p> : <section className="card"><h2>Categorias</h2>
      {!error && categorias.length === 0 && <p>Nenhuma categoria cadastrada.</p>}
      <CategoriaList categorias={categorias} admin={admin} onEdit={setEditing} onDelete={excluir} />
    </section>}
  </>
}
