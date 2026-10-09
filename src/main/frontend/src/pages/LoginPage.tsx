import { useState, type FormEvent } from 'react'
import api from '../services/api'
import type { Sessao } from '../types/Sessao'
import axios from 'axios'
export default function LoginPage({ onLogin }: { onLogin: (session: Sessao) => void }) {
 const [registro,setRegistro]=useState(false)
 const [dados,setDados]=useState({nome:'',username:'',email:'',senha:''})
 const [erro,setErro]=useState(''); const [saving,setSaving]=useState(false)
 async function enviar(e: FormEvent<HTMLFormElement>) {
  e.preventDefault();setSaving(true);setErro('')
  try { const {data}=await api.post<Sessao>(registro ? '/auth/registro' : '/auth/login',dados); onLogin(data) }
  catch(e) {setErro(axios.isAxiosError(e) ? e.response?.data?.message || 'Não foi possível entrar. Confira os dados e o back-end.' : 'Falha ao entrar.')}
  finally {setSaving(false)}
 }
 return <section className="card"><nav><button onClick={()=>{setRegistro(false);setErro('')}}>Entrar</button><button onClick={()=>{setRegistro(true);setErro('')}}>Criar conta</button></nav>
 {erro && <p role="alert" className="alert">{erro}</p>}
 <form onSubmit={enviar}>
 {registro && <><label>Nome<input required value={dados.nome} onChange={e=>setDados({...dados,nome:e.target.value})}/></label><label>Username<input required value={dados.username} onChange={e=>setDados({...dados,username:e.target.value})}/></label></>}
 <label>Email<input required type="email" value={dados.email} onChange={e=>setDados({...dados,email:e.target.value})}/></label>
 <label>Senha<input required type="password" minLength={registro ? 8 : undefined} value={dados.senha} onChange={e=>setDados({...dados,senha:e.target.value})}/></label>
 <button disabled={saving}>{saving ? 'Aguarde...' : registro ? 'Criar conta' : 'Entrar'}</button></form></section>
}
