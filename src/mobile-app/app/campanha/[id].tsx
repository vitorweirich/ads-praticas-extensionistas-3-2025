import React from 'react';
import { View, Text, Image, StyleSheet, ScrollView } from 'react-native';
import { Stack, useLocalSearchParams } from 'expo-router';
import { ThemedView } from '@/components/themed-view';
import { ThemedText } from '@/components/themed-text';
import ProgressBar from '@/components/ProgressBar';
import { getCampanhaById } from '@/services/api';

export default function CampanhaDetalheScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const [campanha, setCampanha] = React.useState<any>(null);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    if (id) {
      getCampanhaById(id)
        .then(setCampanha)
        .finally(() => setLoading(false));
    }
  }, [id]);

  if (loading) {
    return (
      <View style={styles.centered}>
        <Stack.Screen options={{ title: `Detalhes da Campanha` }} />
        <Text>Carregando...</Text>
      </View>
    );
  }
  if (!campanha) {
    return (
      <View style={styles.centered}>
        <Stack.Screen options={{ title: `Campanha não encontrada` }} />
        <Text>Campanha não encontrada.</Text>
      </View>
    );
  }

  function calcularProgresso() {
    if (!campanha.metaFinanceira || campanha.metaFinanceira === 0) return 0;
    return Math.min(
      Math.round((campanha.valorArrecadado / campanha.metaFinanceira) * 100),
      100,
    );
  }
  function formatarValor(valor: number) {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Stack.Screen options={{ title: `Detalhes da Campanha ${id}` }} />
      <Image
        source={{
          uri: campanha.imagemCapa || 'https://via.placeholder.com/300x200?text=Campanha',
        }}
        style={styles.campanhaImg}
        resizeMode="contain"
      />
      <ThemedView style={styles.cardBody}>
        <ThemedText type="title" style={styles.cardTitle}>
          {campanha.nome}
        </ThemedText>
        <ThemedText style={styles.cardText}>{campanha.descricao}</ThemedText>
        <ProgressBar value={calcularProgresso()} />
        <Text style={styles.metaText}>
          Meta: {formatarValor(campanha.metaFinanceira)} | Arrecadado:{' '}
          {formatarValor(campanha.valorArrecadado)}
        </Text>
      </ThemedView>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 16,
    backgroundColor: '#fff',
    flexGrow: 1,
  },
  campanhaImg: {
    width: '100%',
    height: 220,
    backgroundColor: '#f8f9fa',
    borderRadius: 12,
    marginBottom: 16,
  },
  cardBody: {
    padding: 16,
    borderRadius: 12,
    backgroundColor: '#fff',
    elevation: 2,
  },
  cardTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 12,
  },
  cardText: {
    marginBottom: 12,
    fontSize: 16,
  },
  metaText: {
    color: '#6c757d',
    fontSize: 14,
    marginTop: 12,
  },
  centered: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#fff',
  },
});
