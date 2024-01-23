'use client'

import { useAuth } from "@/resources"
import Login from "./login/page"
import GaleriaPage from "./galeria/page"

export default function Home() {

  const auth = useAuth();
  const user = auth.getUserSession();

  if(!user){
    return <Login />
  }

  return (
    <GaleriaPage />
  )
}
