export function calcularProgresso(campanha) {
  if (!campanha.metaFinanceira || campanha.metaFinanceira === 0) return 0;
  const progresso = (campanha.valorArrecadado / campanha.metaFinanceira) * 100;
  return Math.min(Math.round(progresso), 100);
}

export function formatarValor(valor) {
  return valor
    ? valor.toLocaleString("pt-BR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      })
    : "0,00";
}

export function formatarData(data) {
  if (!data) return "-";
  const dataObj = new Date(data);
  return dataObj.toLocaleDateString("pt-BR");
}
