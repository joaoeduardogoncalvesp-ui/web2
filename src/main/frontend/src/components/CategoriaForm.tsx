import { useEffect, useState, type FormEvent } from 'react'
import type { Categoria } from '../types/Categoria'

export interface CategoriaDados { nome: string; descricao: string }
interface Props { categoria: Categoria | null; onSubmit: (dados: CategoriaDados) => Promise<boolean>; onCancel: () => void }
const vazio = { nome: '', descricao: '' }

export default function CategoriaForm({ categoria, onSubmit, onCancel }: Props) {
  const [dados, setDados] = useState<CategoriaDados>(vazio)
  const [saving, setSaving] = useState(false)
  useEffect(() => setDados(categoria ? { nome: categoria.nome, descricao: categoria.descricao || '' } : vazio), [categoria])
  const submit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault(); setSaving(true)
    try { const saved = await onSubmit(dados); if (saved && !categoria) setDados(vazio) } finally { setSaving(false) }
  }
  return <form onSubmit={submit}>
    <label>Nome<input required value={dados.nome} onChange={e => setDados({ ...dados, nome: e.target.value })} /></label>
    <label>Descrição<textarea value={dados.descricao} onChange={e => setDados({ ...dados, descricao: e.target.value })} /></label>
    <div className="actions"><button type="submit" disabled={saving}>{saving ? 'Salvando...' : 'Salvar'}</button>{categoria && <button type="button" onClick={onCancel}>Cancelar</button>}</div>
  </form>
}
