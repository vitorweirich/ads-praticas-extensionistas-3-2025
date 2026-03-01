import { Image } from 'expo-image';
import { StyleSheet, View } from 'react-native';
import ParallaxScrollView from '@/components/parallax-scroll-view';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { useEffect, useState } from 'react';
import { ActivityIndicator, ScrollView } from 'react-native';
import CardCampanha from '../../components/CardCampanha';
import { Campanha, getCampanhasDestaque } from '../../services/api';

export default function HomeScreen() {
  const [campanhas, setCampanhas] = useState<Campanha[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getCampanhasDestaque()
      .then((data) => setCampanhas(data))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    console.log('Campanhas em destaque atualizadas:', campanhas);
  }, [campanhas]);

  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: '#f8f9fa', dark: '#1D3D47' }}
      // headerImage={
      //   <Image
      //     source={require('@/assets/images/partial-react-logo.png')}
      //     style={styles.reactLogo}
      //   />
      // }
    >
      <ThemedView style={styles.jumbotron}>
        <ThemedText type="title" style={styles.title}>
          Faça a diferença hoje
        </ThemedText>
        <ThemedText type="subtitle" style={styles.lead}>
          Apoie campanhas de doação e ajude a transformar vidas em nossa comunidade.
        </ThemedText>
        <ThemedText style={styles.text}>
          Conheça nossas campanhas ativas e descubra como você pode contribuir para um
          mundo melhor.
        </ThemedText>
      </ThemedView>

      <View style={styles.cardsRow}>
        <ThemedView style={styles.card}>
          <ThemedText style={styles.icon}>🤲</ThemedText>
          <ThemedText type="subtitle">Doe</ThemedText>
          <ThemedText style={styles.text}>
            Contribua com nossas campanhas e ajude quem mais precisa. Cada doação faz a
            diferença.
          </ThemedText>
        </ThemedView>
        <ThemedView style={styles.card}>
          <ThemedText style={styles.icon}>💸</ThemedText>
          <ThemedText type="subtitle">Transparência</ThemedText>
          <ThemedText style={styles.text}>
            Acompanhe como os recursos são utilizados. Nosso compromisso é com a
            transparência total.
          </ThemedText>
        </ThemedView>
        <ThemedView style={styles.card}>
          <ThemedText style={styles.icon}>👥</ThemedText>
          <ThemedText type="subtitle">Comunidade</ThemedText>
          <ThemedText style={styles.text}>
            Faça parte de uma comunidade que se importa e trabalha por um futuro melhor
            para todos.
          </ThemedText>
        </ThemedView>
      </View>

      <View style={{ marginTop: 32 }}>
        {(campanhas.length > 0 || loading) && (
          <>
            <ThemedText type="title" style={{ textAlign: 'center', marginBottom: 16 }}>
              Campanhas em Destaque
            </ThemedText>
            {loading ? (
              <ActivityIndicator
                size="large"
                color="#007bff"
                style={{ marginVertical: 24 }}
              />
            ) : (
              <ScrollView>
                {campanhas.map((campanha, idx) => (
                  <CardCampanha key={campanha.id || idx} campanha={campanha} />
                ))}
              </ScrollView>
            )}
          </>
        )}
      </View>
    </ParallaxScrollView>
  );
}

const styles = StyleSheet.create({
  jumbotron: {
    display: 'flex',
    flexDirection: 'column',
    backgroundColor: '#f8f9fa',
    borderRadius: 12,
    padding: 24,
    marginBottom: 24,
    alignItems: 'center',
  },
  title: { fontSize: 28, fontWeight: 'bold', marginBottom: 8 },
  lead: { fontSize: 18, marginBottom: 8, textAlign: 'center' },
  text: { textAlign: 'center' },
  cardsRow: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
    gap: 12,
    marginBottom: 16,
  },
  card: {
    flex: 1,
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    alignItems: 'center',
    marginHorizontal: 4,
    elevation: 2,
  },
  icon: { fontSize: 36, marginBottom: 8 },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: 'absolute',
  },
  campanhaCard: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    elevation: 2,
  },
});
