import { useEffect, useState, type FormEvent } from 'react'
import type { Grupo } from '../types/Grupo'

export interface GrupoDados { nome: string; descricao: string }
interface Props { grupo: Grupo | null; onSubmit: (dados: GrupoDados) => Promise<boolean>; onCancel: () => void }
const vazio = { nome: '', descricao: '' }

export default function GrupoForm({ grupo, onSubmit, onCancel }: Props) {
  const [dados, setDados] = useState<GrupoDados>(vazio)
  const [saving, setSaving] = useState(false)
  useEffect(() => setDados(grupo ? { nome: grupo.nome, descricao: grupo.descricao || '' } : vazio), [grupo])
  const submit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault(); setSaving(true)
    try { const saved = await onSubmit(dados); if (saved && !grupo) setDados(vazio) } finally { setSaving(false) }
  }
  return <form onSubmit={submit}>
    <label>Nome<input required value={dados.nome} onChange={e => setDados({ ...dados, nome: e.target.value })} /></label>
    <label>Descrição<textarea value={dados.descricao} onChange={e => setDados({ ...dados, descricao: e.target.value })} /></label>
    <div className="actions"><button type="submit" disabled={saving}>{saving ? 'Salvando...' : 'Salvar'}</button>{grupo && <button type="button" onClick={onCancel}>Cancelar</button>}</div>
  </form>
}
