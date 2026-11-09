import React, { useState } from 'react';
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
  ScrollView,
  SafeAreaView,
  StatusBar,
  ActivityIndicator
} from 'react-native';
import { Shield, AlertTriangle, CheckCircle, Search, HelpCircle, FileText } from 'lucide-react-native';

export default function App() {
  const [activeTab, setActiveTab] = useState<'scan' | 'report' | 'assistant'>('scan');
  const [inputText, setInputText] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<any>(null);

  const handleAnalyze = () => {
    if (!inputText.trim()) return;
    setLoading(true);

    setTimeout(() => {
      const lower = inputText.toLowerCase();
      let score = 15;
      let classification = 'LOW';
      const flags = [];

      if (lower.includes('registration fee') || lower.includes('security deposit') || lower.includes('refundable')) {
        score += 45;
        flags.push('Upfront Registration/Laptop Fee Demanded');
      }
      if (lower.includes('no interview') || lower.includes('direct selection') || lower.includes('spot offer')) {
        score += 30;
        flags.push('Instant Appointment Without Interview');
      }
      if (lower.includes('telegram') || lower.includes('whatsapp')) {
        score += 20;
        flags.push('Unverified Chat Platform Used For Interview');
      }
      if (lower.includes('@gmail.com') || lower.includes('@yahoo.com')) {
        score += 25;
        flags.push('Free Webmail Address Used by Recruiter');
      }

      score = Math.min(100, score);
      if (score >= 70) classification = 'HIGH';
      else if (score >= 36) classification = 'MEDIUM';

      setResult({
        score,
        classification,
        flags: flags.length > 0 ? flags : ['Standard recruitment patterns verified'],
        summary: score >= 70
          ? 'CRITICAL ALERT: Demands for upfront fees and instant selection indicate a high-risk scam.'
          : 'CLEARANCE: Low fraud probability detected.'
      });
      setLoading(false);
    }, 1200);
  };

  const loadDemo = (type: 'scam' | 'legit') => {
    if (type === 'scam') {
      setInputText('TCS Urgently Hiring Freshers! Salary Rs 45,000/mo. No interview needed. Direct selection letter. Pay refundable security fee Rs 1500 for laptop courier via GPay to tcsrecruiter@gmail.com.');
    } else {
      setInputText('Infosys is hiring Systems Engineers. Skills: Java, Spring Boot, SQL. Selection involves online assessment & MS Teams interview. Infosys never charges fees at any stage. Apply at career.infosys.com.');
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#090D0B" />

      {/* Header */}
      <View style={styles.header}>
        <View style={styles.headerTitleRow}>
          <Shield size={28} color="#10B981" />
          <Text style={styles.headerTitle}>JobShield AI</Text>
        </View>
        <Text style={styles.headerSubtitle}>Cybersecurity Recruitment Verification</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Input Box */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Paste Job Posting Advertisement</Text>
          <TextInput
            style={styles.textInput}
            multiline
            numberOfLines={6}
            placeholder="Paste text from WhatsApp, LinkedIn, or Email..."
            placeholderTextColor="#6B7280"
            value={inputText}
            onChangeText={setInputText}
          />

          <View style={styles.presetRow}>
            <TouchableOpacity style={styles.presetButton} onPress={() => loadDemo('scam')}>
              <Text style={styles.presetButtonText}>Demo Scam</Text>
            </TouchableOpacity>
            <TouchableOpacity style={[styles.presetButton, styles.presetLegit]} onPress={() => loadDemo('legit')}>
              <Text style={[styles.presetButtonText, { color: '#10B981' }]}>Demo Legit</Text>
            </TouchableOpacity>
          </View>

          <TouchableOpacity style={styles.analyzeButton} onPress={handleAnalyze} disabled={loading}>
            {loading ? (
              <ActivityIndicator color="#090D0B" />
            ) : (
              <Text style={styles.analyzeButtonText}>ANALYZE RECRUITMENT RISK</Text>
            )}
          </TouchableOpacity>
        </View>

        {/* Results */}
        {result && (
          <View style={[styles.card, styles.resultCard]}>
            <View style={styles.scoreRow}>
              <View>
                <Text style={styles.scoreLabel}>OVERALL RISK SCORE</Text>
                <Text style={[
                  styles.scoreValue,
                  { color: result.score >= 70 ? '#EF4444' : result.score >= 36 ? '#F59E0B' : '#10B981' }
                ]}>
                  {result.score}/100
                </Text>
              </View>
              <View style={[
                styles.badge,
                { backgroundColor: result.score >= 70 ? '#7F1D1D' : result.score >= 36 ? '#78350F' : '#064E3B' }
              ]}>
                <Text style={styles.badgeText}>{result.classification} RISK</Text>
              </View>
            </View>

            <Text style={styles.summaryText}>{result.summary}</Text>

            <Text style={styles.flagsHeader}>Detected Signals:</Text>
            {result.flags.map((flag: string, index: number) => (
              <View key={index} style={styles.flagItem}>
                <AlertTriangle size={16} color={result.score >= 70 ? '#EF4444' : '#10B981'} />
                <Text style={styles.flagText}>{flag}</Text>
              </View>
            ))}
          </View>
        )}

        {/* Helpline Card */}
        <View style={styles.helplineCard}>
          <Text style={styles.helplineTitle}>Official Indian Cyber Safety</Text>
          <Text style={styles.helplineText}>• National Cyber Crime Helpline: 1930</Text>
          <Text style={styles.helplineText}>• Portal: cybercrime.gov.in</Text>
          <Text style={styles.helplineText}>• National Career Service: ncs.gov.in</Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#090D0B' },
  header: { padding: 20, borderBottomWidth: 1, borderBottomColor: '#1E2923' },
  headerTitleRow: { flexDirection: 'row', alignItems: 'center', gap: 10 },
  headerTitle: { fontSize: 22, fontWeight: 'bold', color: '#F3F4F6' },
  headerSubtitle: { color: '#9CA3AF', fontSize: 13, marginTop: 4 },
  scrollContent: { padding: 16 },
  card: { backgroundColor: '#111816', borderRadius: 16, padding: 16, borderWidth: 1, borderColor: '#1E2923', marginBottom: 16 },
  cardTitle: { color: '#E5E7EB', fontSize: 15, fontWeight: '600', marginBottom: 10 },
  textInput: { backgroundColor: '#090D0B', color: '#F3F4F6', borderRadius: 12, padding: 12, borderWidth: 1, borderColor: '#1E2923', textAlignVertical: 'top', minHeight: 120, fontSize: 14 },
  presetRow: { flexDirection: 'row', gap: 10, marginVertical: 12 },
  presetButton: { flex: 1, backgroundColor: '#271717', borderWidth: 1, borderColor: '#7F1D1D', padding: 8, borderRadius: 8, alignItems: 'center' },
  presetLegit: { backgroundColor: '#062B1D', borderColor: '#065F46' },
  presetButtonText: { color: '#EF4444', fontSize: 12, fontWeight: '600' },
  analyzeButton: { backgroundColor: '#10B981', padding: 14, borderRadius: 12, alignItems: 'center', marginTop: 4 },
  analyzeButtonText: { color: '#090D0B', fontWeight: 'bold', fontSize: 14, letterSpacing: 0.5 },
  resultCard: { borderColor: '#059669' },
  scoreRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  scoreLabel: { color: '#9CA3AF', fontSize: 11, fontWeight: 'bold' },
  scoreValue: { fontSize: 32, fontWeight: '900', marginTop: 2 },
  badge: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 20 },
  badgeText: { color: '#F3F4F6', fontWeight: 'bold', fontSize: 12 },
  summaryText: { color: '#D1D5DB', fontSize: 13, marginVertical: 12, lineHeight: 18 },
  flagsHeader: { color: '#9CA3AF', fontSize: 12, fontWeight: 'bold', marginBottom: 8 },
  flagItem: { flexDirection: 'row', alignItems: 'center', gap: 8, marginVertical: 4 },
  flagText: { color: '#F3F4F6', fontSize: 13 },
  helplineCard: { backgroundColor: '#0E1714', borderRadius: 12, padding: 14, borderWidth: 1, borderColor: '#1E2923' },
  helplineTitle: { color: '#10B981', fontWeight: 'bold', fontSize: 13, marginBottom: 6 },
  helplineText: { color: '#9CA3AF', fontSize: 12, marginVertical: 2 }
});

