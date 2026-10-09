import type { Usuario } from '../types/Usuario'
interface Props { item: Usuario; admin: boolean; onEdit: (item: Usuario) => void; onDelete: (item: Usuario) => void }
export default function UsuarioItem({item, admin, onEdit, onDelete}: Props) {
 return <article><div><strong>{item.nome}</strong><p>@{item.username} · {item.email} · {item.papel}</p></div>{admin && <div className="actions"><button onClick={() => onEdit(item)}>Editar</button><button onClick={() => onDelete(item)}>Excluir</button></div>}</article>
}
