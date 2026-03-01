import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

interface ProgressBarProps {
  value: number; // 0-100
  height?: number;
  showText?: boolean;
  barColor?: string;
  backgroundColor?: string;
}

const ProgressBar: React.FC<ProgressBarProps> = ({
  value,
  height = 16,
  showText = true,
  barColor = '#007bff',
  backgroundColor = '#e9ecef',
}) => {
  return (
    <View style={[styles.progress, { height, backgroundColor }]}>  
      <View
        style={[
          styles.progressBar,
          {
            width: `${Math.max(0, Math.min(100, value))}%`,
            backgroundColor: barColor,
          },
        ]}
      />
      {showText && (
        <Text
          style={[
            styles.progressText,
            { color: value >= 50 ? '#fff' : '#212529' },
          ]}
        >
          {value}%
        </Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  progress: {
    width: '100%',
    borderRadius: 8,
    overflow: 'hidden',
    marginBottom: 12,
    position: 'relative',
    justifyContent: 'center',
  },
  progressBar: {
    position: 'absolute',
    left: 0,
    top: 0,
    bottom: 0,
    borderRadius: 8,
  },
  progressText: {
    position: 'absolute',
    left: '50%',
    top: '50%',
    transform: [{ translateX: -16 }, { translateY: -8 }],
    fontWeight: '600',
    fontSize: 13,
    pointerEvents: 'none',
    textAlign: 'center',
  },
});

export default ProgressBar;
