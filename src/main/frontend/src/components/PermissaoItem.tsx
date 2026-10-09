import type { Permissao } from '../types/Permissao'
interface Props { item: Permissao; admin: boolean; onEdit: (item: Permissao) => void; onDelete: (item: Permissao) => void }
export default function PermissaoItem({item, admin, onEdit, onDelete}: Props) {
 return <article><div><strong>{item.nome}</strong><p>{item.descricao}</p></div>{admin && <div className="actions"><button onClick={() => onEdit(item)}>Editar</button><button onClick={() => onDelete(item)}>Excluir</button></div>}</article>
}
