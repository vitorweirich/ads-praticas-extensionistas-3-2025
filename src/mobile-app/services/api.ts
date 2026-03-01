// src/services/api.ts
// Central API config and endpoints for the mobile app

export interface Campanha {
  id: string | number;
  nome: string;
  status: string;
  descricao: string;
  imagemCapa?: string;
  metaFinanceira: number;
  valorArrecadado: number;
}

const API_BASE_URL = 'http://10.0.2.2:8080'; // Altere para a URL real
const API_BASE_URL_REAL = 'http://localhost:8080'; // Altere para a URL real

const ngrok_headers = {
  'ngrok-skip-browser-warning': 'true',
};

export async function getCampanhasDestaque(): Promise<Campanha[]> {
  try {
    const res = await fetch(`${API_BASE_URL}/api/campanhas/publicas`, {
      headers: ngrok_headers,
    });
    if (!res.ok) throw new Error('Erro ao buscar campanhas');
    const data = await res.json();
    // Filtra campanhas ativas e retorna até 3
    return (data || [])
      .filter((c: Campanha) => c.status === 'ATIVA')
      .slice(0, 3)
      .map((c: Campanha) => ({
        ...c,
        imagemCapa: c?.imagemCapa?.replace(API_BASE_URL_REAL, API_BASE_URL),
      }));
  } catch (e) {
    console.error('Erro na API:', e);
    return [];
  }
}

export async function getCampanhaById(id: string | number): Promise<Campanha | null> {
  try {
    const res = await fetch(`${API_BASE_URL}/api/campanhas/${id}`, {
      headers: ngrok_headers,
    });
    if (!res.ok) throw new Error('Erro ao buscar campanha');
    const c = await res.json();
    return {
      ...c,
      imagemCapa: c?.imagemCapa?.replace(API_BASE_URL_REAL, API_BASE_URL),
    };
  } catch (e) {
    console.error('Erro na API:', e);
    return null;
  }
}
