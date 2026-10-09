import type { Grupo } from '../types/Grupo'
interface Props { item: Grupo; admin: boolean; onEdit: (item: Grupo) => void; onDelete: (item: Grupo) => void }
export default function GrupoItem({item, admin, onEdit, onDelete}: Props) {
 return <article><div><strong>{item.nome}</strong><p>{item.descricao}</p></div>{admin && <div className="actions"><button onClick={() => onEdit(item)}>Editar</button><button onClick={() => onDelete(item)}>Excluir</button></div>}</article>
}
