import type { Permissao } from '../types/Permissao'
import PermissaoItem from './PermissaoItem'
interface Props { permissoes: Permissao[]; admin: boolean; onEdit: (item: Permissao) => void; onDelete: (item: Permissao) => void }
export default function PermissaoList({ permissoes, ...props }: Props) {
 return <>{permissoes.map(item => <PermissaoItem key={item.id} item={item} {...props} />)}</>
}
