import type { Categoria } from '../types/Categoria'
interface Props { item: Categoria; admin: boolean; onEdit: (item: Categoria) => void; onDelete: (item: Categoria) => void }
export default function CategoriaItem({item, admin, onEdit, onDelete}: Props) {
 return <article><div><strong>{item.nome}</strong><p>{item.descricao}</p></div>{admin && <div className="actions"><button onClick={() => onEdit(item)}>Editar</button><button onClick={() => onDelete(item)}>Excluir</button></div>}</article>
}
