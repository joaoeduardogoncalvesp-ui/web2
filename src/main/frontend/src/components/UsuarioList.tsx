import type { Usuario } from '../types/Usuario'
import UsuarioItem from './UsuarioItem'
interface Props { usuarios: Usuario[]; admin: boolean; onEdit: (item: Usuario) => void; onDelete: (item: Usuario) => void }
export default function UsuarioList({ usuarios, ...props }: Props) {
 return <>{usuarios.map(item => <UsuarioItem key={item.id} item={item} {...props} />)}</>
}
