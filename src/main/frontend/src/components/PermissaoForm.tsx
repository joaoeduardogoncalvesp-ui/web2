import { useEffect, useState, type FormEvent } from 'react'
import type { Permissao } from '../types/Permissao'

export interface PermissaoDados { nome: string; descricao: string }
interface Props { permissao: Permissao | null; onSubmit: (dados: PermissaoDados) => Promise<boolean>; onCancel: () => void }
const vazio = { nome: '', descricao: '' }

export default function PermissaoForm({ permissao, onSubmit, onCancel }: Props) {
  const [dados, setDados] = useState<PermissaoDados>(vazio)
  const [saving, setSaving] = useState(false)
  useEffect(() => setDados(permissao ? { nome: permissao.nome, descricao: permissao.descricao || '' } : vazio), [permissao])
  const submit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault(); setSaving(true)
    try { const saved = await onSubmit(dados); if (saved && !permissao) setDados(vazio) } finally { setSaving(false) }
  }
  return <form onSubmit={submit}>
    <label>Nome<input required value={dados.nome} onChange={e => setDados({ ...dados, nome: e.target.value })} /></label>
    <label>Descrição<textarea value={dados.descricao} onChange={e => setDados({ ...dados, descricao: e.target.value })} /></label>
    <div className="actions"><button type="submit" disabled={saving}>{saving ? 'Salvando...' : 'Salvar'}</button>{permissao && <button type="button" onClick={onCancel}>Cancelar</button>}</div>
  </form>
}
