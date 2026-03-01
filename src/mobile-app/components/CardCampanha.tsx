import React from 'react';
import { View, Text, Image, StyleSheet, Pressable } from 'react-native';
import { useRouter } from 'expo-router';
import { ThemedView } from '@/components/themed-view';
import { ThemedText } from '@/components/themed-text';
import ProgressBar from '@/components/ProgressBar';
import { Campanha } from '@/services/api';

interface CardCampanhaProps {
  campanha: Campanha;
}

function calcularProgresso(campanha: CardCampanhaProps['campanha']) {
  if (!campanha.metaFinanceira || campanha.metaFinanceira === 0) return 0;
  return Math.min(
    Math.round((campanha.valorArrecadado / campanha.metaFinanceira) * 100),
    100,
  );
}

function formatarValor(valor: number) {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

const CardCampanha: React.FC<CardCampanhaProps> = ({ campanha }) => {
  const router = useRouter();

  return (
    <Pressable
      onPress={() =>
        router.push({ pathname: `/campanha/[id]`, params: { id: campanha.id } })
      }
      style={({ pressed }) => [styles.pressable, pressed && { opacity: 0.85 }]}
      android_ripple={{ color: '#e9ecef' }}
    >
      <ThemedView style={styles.cardCampanha}>
        <Image
          source={{
            uri:
              campanha.imagemCapa || 'https://via.placeholder.com/300x200?text=Campanha',
          }}
          style={styles.campanhaImg}
          resizeMode="contain"
        />
        <View style={styles.cardBody}>
          <ThemedText type="subtitle" style={styles.cardTitle}>
            {campanha.nome}
          </ThemedText>
          <ThemedText style={styles.cardText}>
            {campanha.descricao.substring(0, 100)}...
          </ThemedText>
          <ProgressBar value={calcularProgresso(campanha)} />
          <Text style={styles.metaText}>
            Meta: {formatarValor(campanha.metaFinanceira)} | Arrecadado:{' '}
            {formatarValor(campanha.valorArrecadado)}
          </Text>
        </View>
      </ThemedView>
    </Pressable>
  );
};

const styles = StyleSheet.create({
  cardCampanha: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 0,
    marginBottom: 12,
    elevation: 2,
    overflow: 'hidden',
  },
  campanhaImg: {
    width: '100%',
    height: 180,
    backgroundColor: '#f8f9fa',
  },
  cardBody: {
    padding: 16,
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  cardText: {
    marginBottom: 8,
  },
  metaText: {
    color: '#6c757d',
    fontSize: 12,
    marginTop: 8,
  },
  pressable: {
    width: '100%',
  },
});

export default CardCampanha;
