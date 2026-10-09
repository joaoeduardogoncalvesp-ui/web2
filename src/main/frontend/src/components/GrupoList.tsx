import type { Grupo } from '../types/Grupo'
import GrupoItem from './GrupoItem'
interface Props { grupos: Grupo[]; admin: boolean; onEdit: (item: Grupo) => void; onDelete: (item: Grupo) => void }
export default function GrupoList({ grupos, ...props }: Props) {
 return <>{grupos.map(item => <GrupoItem key={item.id} item={item} {...props} />)}</>
}
