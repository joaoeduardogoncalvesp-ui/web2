import { useState } from 'react'
import type { Sessao } from '../types/Sessao'
import LoginPage from './LoginPage'
import UsuariosPage from './UsuariosPage'
import PermissoesPage from './PermissoesPage'
import GruposPage from './GruposPage'
import CategoriasPage from './CategoriasPage'
export default function SistemaPage() {
 const [session,setSession]=useState<Sessao | null>(()=>{try { const value=JSON.parse(sessionStorage.getItem('session') || 'null'); return value?.token && value?.usuario ? value : null } catch {return null}})
 const [tab,setTab]=useState('categorias')
 const admin=session?.usuario.papel==='ADMIN'
 const login=(s: Sessao)=>{sessionStorage.setItem('session',JSON.stringify(s));setSession(s)}
 return <main><header><div><h1>Projeto Fullstack</h1><p>Desenvolvimento Web II • Usuários, permissões e categorias</p></div>{session && <button onClick={()=>{sessionStorage.removeItem('session');setSession(null);setTab('categorias')}}>Sair</button>}</header>
 {!session ? <LoginPage onLogin={login}/> : <><p>Olá, {session.usuario.nome} ({session.usuario.papel})</p><nav>
 {admin && <button className={tab==='usuarios' ? 'active' : ''} onClick={()=>setTab('usuarios')}>Usuários</button>}
 <button className={tab==='permissoes' ? 'active' : ''} onClick={()=>setTab('permissoes')}>Permissões</button>
 <button className={tab==='grupos' ? 'active' : ''} onClick={()=>setTab('grupos')}>Grupos</button>
 <button className={tab==='categorias' ? 'active' : ''} onClick={()=>setTab('categorias')}>Categorias</button></nav>
 {tab==='grupos' && <GruposPage admin={admin}/>}{tab==='usuarios' && admin && <UsuariosPage/>}{tab==='permissoes' && <PermissoesPage admin={admin}/>}{tab==='categorias' && <CategoriasPage admin={admin}/>}</>}
 </main>
}
