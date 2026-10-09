import type { Categoria } from '../types/Categoria'
import CategoriaItem from './CategoriaItem'
interface Props { categorias: Categoria[]; admin: boolean; onEdit: (item: Categoria) => void; onDelete: (item: Categoria) => void }
export default function CategoriaList({ categorias, ...props }: Props) {
 return <>{categorias.map(item => <CategoriaItem key={item.id} item={item} {...props} />)}</>
}
